package ga.discoveryandlost.discoveryandlost.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;
import com.github.ppamorim.dragger.DraggerPosition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.Classifier;
import ga.discoveryandlost.discoveryandlost.Detecter;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.util.AdditionalFunc;
import ga.discoveryandlost.discoveryandlost.util.OnDetecterListener;


public class CameraActivity extends BaseActivity implements OnDetecterListener {

    private MyHandler handler = new MyHandler();
    private final int MSG_MESSAGE_SHOW_PROGRESS = 500;
    private final int MSG_MESSAGE_HIDE_PROGRESS = 501;
    private final int MSG_MESSAGE_ERROR_DIALOG = 502;
    private final int MSG_MESSAGE_CHANGE_PROGRESS = 503;

    private Detecter detecter;

    private CameraView cameraView;
    private CardView btnDetect;

    public static byte[] photo;

    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        init();

    }

    private void init(){

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.this.finish();
            }
        });

        detecter = new Detecter(getApplicationContext(), this);

        initProgressDialog();

        cameraView = (CameraView)findViewById(R.id.cameraView);
        btnDetect = (CardView)findViewById(R.id.cv_take_photo);
        setCardButtonOnTouchAnimation(btnDetect);

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.captureImage();
            }
        });

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        // cameraview library has its own permission check method
        cameraView.setPermissions(CameraKit.Constants.PERMISSIONS_PICTURE);

        // invoke tensorflow inference when picture taken from camera
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(final byte[] picture) {
                super.onPictureTaken(picture);

                handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_SHOW_PROGRESS));

                new Thread(){
                    @Override
                    public void run(){

                        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                        photo = picture;
                        if(bitmap != null) {
                            detecter.recognize_bitmap(bitmap);
                        }

                    }
                }.start();

            }
        });

        cameraView.start();

    }

    private void initProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .theme(Theme.LIGHT)
                .cancelable(false)
                .build();
    }

    private class MyHandler extends Handler {

        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_MESSAGE_SHOW_PROGRESS:
                    progressDialog.show();
                    break;
                case MSG_MESSAGE_HIDE_PROGRESS:
                    progressDialog.hide();
                    break;
                case MSG_MESSAGE_CHANGE_PROGRESS:
                    progressDialog.setTitle("이미지 인식중입니다.");
                    break;
                case MSG_MESSAGE_ERROR_DIALOG:
                    new MaterialDialog.Builder(CameraActivity.this)
                            .title(R.string.fail_srt)
                            .content("인식에 실패하였습니다.")
                            .positiveText(R.string.ok)
                            .show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.stop();
        detecter.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDetectFinish(List<Classifier.Recognition> results) {
//        showSnackbar(results.toString());
//        handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_HIDE_PROGRESS));
        ArrayList<Classifier.Recognition> list = new ArrayList<>();
        list.addAll(results);
        if(list.size() > 0) {
            handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_CHANGE_PROGRESS));
            loadAttractionInfo(list);
//            Intent intent = new Intent(StartActivity.this, DetailActivity.class);
//            intent.putExtra("drag_position", DraggerPosition.TOP);
//            intent.putExtra("data", list);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
        }else{
            handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_ERROR_DIALOG));
        }
    }

    private String saveTempImage(){

        if(photo.length > 0){
            File f3=new File(Environment.getExternalStorageDirectory()+"/dal/temp/");
            if(!f3.exists())
                f3.mkdirs();
            OutputStream outStream = null;
            String fileName = "temporarily" + CameraActivity.photo.length + ".jpg";
            File file = new File(Environment.getExternalStorageDirectory() + "/dal/temp/"+fileName);
            try {
                outStream = new FileOutputStream(file);
                Bitmap mBitmap = BitmapFactory.decodeByteArray(CameraActivity.photo, 0, CameraActivity.photo.length);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();
                //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fileName;

        }

        return "";

    }

    private void loadAttractionInfo(final ArrayList<Classifier.Recognition> list){

        String item = list.get(0).getTitle();

//        attraction = AdditionalFunc.convertAttraction(attraction);
//        switch (attraction){
//            case "roses":
//                attraction = "1898 명동성당";
//                break;
//            case "daisy":
//                attraction = "DDP(동대문디자인플라자)";
//                break;
//            case "dandelion":
//                attraction = "경복궁";
//                break;
//            case "sunflowers":
//                attraction = "경희궁";
//                break;
//            default:
//                attraction = "N서울타워";
//                break;
//        }

        String imageName = saveTempImage();

        Intent intent = new Intent(CameraActivity.this, RegisterNewItemActivity.class);
        intent.putExtra("item", item);
        intent.putExtra("imageName", imageName);
        startActivity(intent);
        finish();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("service", "getLocationInfo");
//        map.put("query", item);
//        new ParsePHP(Information.MAIN_SERVER_ADDRESS, map) {
//
//            @Override
//            protected void afterThreadFinish(String data) {
//
//                Attraction att = AdditionalFunc.getAttractionInfo(data);
//
//                handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_HIDE_PROGRESS));
//
//                Intent intent = new Intent(CameraActivity.this, DetailActivity.class);
//                intent.putExtra("drag_position", DraggerPosition.TOP);
//                intent.putExtra("data", list);
//                intent.putExtra("attraction", att);
//                startActivity(intent);
//
//            }
//        }.start();

    }

}
