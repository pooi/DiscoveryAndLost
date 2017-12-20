package ga.discoveryandlost.discoveryandlost.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    private String photoColor;

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

                int pixel = mBitmap.getPixel(mBitmap.getWidth()/2, mBitmap.getHeight()/2);
                int redValue1 = Color.red(pixel);
                int blueValue1 = Color.blue(pixel);
                int greenValue1 = Color.green(pixel);
                int thiscolor1 = Color.rgb(redValue1, greenValue1, blueValue1);
                System.out.println(String.format("pixel : %d, red : %d, blue : %d, green : %d, thiscolor : %d", pixel, redValue1, blueValue1, greenValue1, thiscolor1));

                detectColor(mBitmap);

                outStream.close();
                //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fileName;

        }

        return "";

    }

    private void detectColor(Bitmap bitmap){

        String COLOR = "빨간색,230,25,75/초록색,60,180,75/노란색,255,225,25/파란색,0,130,200/주황색,245,130,48/보라색,145,30,180/하늘색,70,240,240/분홍색,240,50,230/연두색,210,245,60/분홍색,250,190,190/청록색,0,128,128/연조라색,230,190,255/갈색,170,110,40/베이지색,255,250,200/검붉은색,128,0,0/민트색,170,255,195/카키색,128,128,0/살색,255,215,180/남색,0,0,128/회색,128,128,128/하얀색,255,255,255/검은색,0,0,0";

        int r = 0, g = 0, b = 0;

        int cx, cy;
        cx = bitmap.getWidth()/2;
        cy = bitmap.getHeight()/2;

        int count = 0;
        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                count += 1;
                int px, py;
                px = cx + i * 10;
                py = cy + j * 10;

                int pixel = bitmap.getPixel(px, py);
                int redValue1 = Color.red(pixel);
                int blueValue1 = Color.blue(pixel);
                int greenValue1 = Color.green(pixel);

                r += redValue1;
                g += greenValue1;
                b += blueValue1;

            }
        }

        r /= count;
        g /= count;
        b /= count;
        b *= 0.9;

        String color = "";
        double distance = 100000000000.0;

        for(String strs : COLOR.split("/")){

            String[] rgbs = strs.split(",");
            String c = rgbs[0];
            int rTemp = Integer.parseInt(rgbs[1]);
            int gTemp = Integer.parseInt(rgbs[2]);
            int bTemp = Integer.parseInt(rgbs[3]);

            double d = Math.sqrt(Math.pow(rTemp - r,2) + Math.pow(gTemp - g,2) + Math.pow(bTemp - b,2));

            if(d < distance){
                color = c;
                distance = d;
            }

        }

        photoColor = color;
        System.out.println(color);

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
        intent.putExtra("color", photoColor);
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
