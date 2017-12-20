package ga.discoveryandlost.discoveryandlost.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.matthewtamlin.sliding_intro_screen_library.indicators.DotIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.CustomApplication;
import ga.discoveryandlost.discoveryandlost.Information;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.StartActivity;
import ga.discoveryandlost.discoveryandlost.fragment.RegisterDetailFragment;
import ga.discoveryandlost.discoveryandlost.fragment.RegisterSubmitFragment;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.util.CustomViewPager;
import ga.discoveryandlost.discoveryandlost.util.RegisterSelectListener;

public class RegisterNewItemActivity extends BaseActivity implements RegisterSelectListener{

    private MyHandler handler = new MyHandler();
    private final int MSG_MESSAGE_SUCCESS = 500;
    private final int MSG_MESSAGE_FAIL = 501;

    String itemName;

//    private DotIndicator dotIndicator;
    private CustomViewPager viewPager;
    private NavigationAdapter mPagerAdapter;
    private RegisterDetailFragment[] registerDetailFragments;
    private RegisterSubmitFragment submitFragment;

    DalItem item;

    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_item);

        itemName = getIntent().getStringExtra("item");
        String color = getIntent().getStringExtra("color");
        item = new DalItem();
        String imageName = getIntent().getStringExtra("imageName");

        item.setCategory(itemName);
        item.setTempImageName(imageName);
        item.setColor(color);

        registerDetailFragments = new RegisterDetailFragment[item.getSize()];

        init();

    }

    private void init(){

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterNewItemActivity.this.finish();
            }
        });

        initProgressDialog();

//        dotIndicator = (DotIndicator) findViewById(R.id.main_indicator_ad);
//        dotIndicator.setSelectedDotColor(Color.parseColor("#FF4081"));
//        dotIndicator.setUnselectedDotColor(Color.parseColor("#CFCFCF"));
//        dotIndicator.setNumberOfItems(registerDetailFragments.length+1);
//        dotIndicator.setSelectedItem(0, false);
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(registerDetailFragments.length+1);
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager(), item, this);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if(position == lastPage+1){
//                    viewPager.setCurrentItem(lastPage, true);
//                }
            }

            @Override
            public void onPageSelected(int position) {
//                dotIndicator.setSelectedItem(position, true);
                if(submitFragment != null)
                    submitFragment.updateContent();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    private void removeAllImage(){

        final File dir = new File(Environment.getExternalStorageDirectory(), "/dal/temp");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            if(children != null) {
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        }

    }

    public void setReviewItemFragments(RegisterDetailFragment f, int position){
        registerDetailFragments[position] = f;
    }
    public RegisterDetailFragment getReviewItemFragment(int position){
        return registerDetailFragments[position];
    }
    public void setRegisterSubmitFragment(RegisterSubmitFragment f){
        this.submitFragment = f;
    }
    public RegisterSubmitFragment getSubmitFragment(){
        return this.submitFragment;
    }

    @Override
    public void select(final int fragmentPosition, View v, int selectAnswerIndex, boolean force) {
        item.updateContent(v, fragmentPosition);
        submitFragment.updateContent();

        Runnable runnable = new Runnable() {
            public void run() {
                if(fragmentPosition+1 < item.getSize())
                    viewPager.setCurrentItem(fragmentPosition + 1, true);
            }
        };

        if(force) {
            handler.postDelayed(runnable, 0);
        }else{
            handler.postDelayed(runnable, 500);
        }
    }

    @Override
    public void submit() {

        progressDialog.show();

        File imgFile = new  File(Environment.getExternalStorageDirectory() + "/dal/temp/"+item.getTempImageName());

        Bitmap bitmap = null;

        if(imgFile.exists()){

            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

//            myImage.setImageBitmap(myBitmap);

        }

        final boolean flagRemoveImg = bitmap != null;

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Information.MAIN_SERVER_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");

                            if("success".equals(status)){
                                handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_SUCCESS));
                            }else{
                                handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_FAIL));
                            }

                            if(flagRemoveImg){
                                removeAllImage();
                            }

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_FAIL));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addStringParam("service", "registerLost");



        if(bitmap != null){
            smr.addStringParam("save_photo", "1");
            File f3=new File(Environment.getExternalStorageDirectory()+"/dal/temp/");
            if(!f3.exists())
                f3.mkdirs();
            OutputStream outStream = null;
            File file = new File(Environment.getExternalStorageDirectory() + "/dal/temp/"+item.getTempImageName());
            try {
                outStream = new FileOutputStream(file);
                Bitmap mBitmap = BitmapFactory.decodeByteArray(CameraActivity.photo, 0, CameraActivity.photo.length);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();
                //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            smr.addFile("image", file.getPath());

        }

        smr.addStringParam("rgt_user", StartActivity.USER.getId());
        smr.addStringParam("category", item.getCategory() == null ? "" : item.getCategory());
        smr.addStringParam("color", item.getColor() == null ? "" : item.getColor());
        smr.addStringParam("brand", item.getBrand() == null ? "" : item.getBrand());
        smr.addStringParam("building", item.getBuilding() == null ? "" : item.getBuilding());
        smr.addStringParam("room", item.getRoom() == null ? "" : item.getRoom());
        smr.addStringParam("tags", item.getTags() == null ? "" : item.getTags());
        smr.addStringParam("description", item.getDescription() == null ? "" : item.getDescription());


        CustomApplication.getInstance().addToRequestQueue(smr);

    }


    private class MyHandler extends Handler implements Serializable {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MESSAGE_SUCCESS:
                    progressDialog.hide();
                    new MaterialDialog.Builder(RegisterNewItemActivity.this)
                            .title(R.string.success_srt)
                            .content(R.string.successfully_recorded)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    RegisterNewItemActivity.this.finish();
                                }
                            })
                            .show();
                    break;
                case MSG_MESSAGE_FAIL:
                    progressDialog.hide();
                    new MaterialDialog.Builder(RegisterNewItemActivity.this)
                            .title(R.string.fail_srt)
                            .positiveText(R.string.ok)
                            .show();
                    break;
                default:
                    break;
            }
        }
    }

    private static class NavigationAdapter extends FragmentPagerAdapter {

        private int size;
        private DalItem item;
        private RegisterNewItemActivity activity;

        public NavigationAdapter(FragmentManager fm, DalItem item, RegisterNewItemActivity activity) {
            super(fm);
            this.item = item;
            this.size = item.getSize()+1;
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {

            final int pattern = position % size;

            Fragment f;


            Bundle bdl = new Bundle(1);

            if(pattern == size-1){

                if(activity.getSubmitFragment() == null) {

                    f = new RegisterSubmitFragment();
                    bdl.putInt("position", pattern);
                    bdl.putSerializable("item", item);
                    bdl.putSerializable("listener", activity);


                    activity.setRegisterSubmitFragment((RegisterSubmitFragment) f);

                }else{
                    f = activity.getSubmitFragment();
                }

            }else {


                if(activity.getReviewItemFragment(pattern) == null){

                    f = new RegisterDetailFragment();
                    bdl.putInt("position", pattern);
                    bdl.putSerializable("item", item);
                    bdl.putSerializable("listener", activity);

                    activity.setReviewItemFragments((RegisterDetailFragment)f, pattern);

                }else {
                    f = activity.getReviewItemFragment(pattern);
                }

            }

//                if(pattern == size-1){
//                    //ReviewSelectListener listener = (ReviewSelectListener)activity;
////                    bdl.putSerializable("listener", activity);
////                    bdl.putBoolean("isSubmitMode", true);
//                }else{
//                    bdl.putInt("position", pattern);
////                    bdl.putSerializable("item", list.get(pattern));
//                    //ReviewSelectListener listener = (ReviewSelectListener)activity;
////                    bdl.putSerializable("listener", activity);
////                    if(pattern == size-2){
////                        bdl.putBoolean("isEditMode", true);
////                    }
//                }

            f.setArguments(bdl);

            return f;
        }

        @Override
        public int getCount() {
            return size;
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
