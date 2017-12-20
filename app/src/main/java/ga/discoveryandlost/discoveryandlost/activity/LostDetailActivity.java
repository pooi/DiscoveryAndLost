package ga.discoveryandlost.discoveryandlost.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.CustomApplication;
import ga.discoveryandlost.discoveryandlost.Information;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.StartActivity;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.obj.DalLost;
import ga.discoveryandlost.discoveryandlost.obj.DalRequest;
import ga.discoveryandlost.discoveryandlost.util.AdditionalFunc;
import ga.discoveryandlost.discoveryandlost.util.ParsePHP;

public class LostDetailActivity extends BaseActivity {

    private MyHandler handler = new MyHandler();
    private final int MSG_MESSAGE_SUCCESS = 500;
    private final int MSG_MESSAGE_FAIL = 501;
    private final int MSG_MESSAGE_MAKE_LIST = 502;

    DalLost lost;

    private RoundedImageView img;
    private TextView tv_category;
    private TextView tv_color;
    private TextView tv_brand;
    private TextView tv_building;
    private TextView tv_tags;
    private TextView tv_date;

    private TextView tv_rgt_name;
    private TextView tv_rgt_studentId;
    private TextView tv_rgt_phone;

    private TextView tv_msg;

    private LinearLayout li_request;
    private ArrayList<DalRequest> requests;

    private MaterialDialog progressDialog;
    private AVLoadingIndicatorView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_detail);

        Intent intent = getIntent();
        lost = (DalLost)intent.getSerializableExtra("lost");
        requests = new ArrayList<>();

        init();

    }

    private void init(){

        initProgressDialog();
        loading = (AVLoadingIndicatorView)findViewById(R.id.loading);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LostDetailActivity.this.finish();
            }
        });

        img = (RoundedImageView)findViewById(R.id.img);
        tv_category = (TextView)findViewById(R.id.tv_category);
        tv_color = (TextView)findViewById(R.id.tv_color);
        tv_brand = (TextView)findViewById(R.id.tv_brand);
        tv_building = (TextView)findViewById(R.id.tv_building);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_tags = (TextView)findViewById(R.id.tv_tags);

        String photo = lost.getPhotos();
        if(photo == null)
            photo = "";
        Picasso.with(getApplicationContext())
                .load(photo.isEmpty() ? getImgUrl("") : photo)
                .into(img);
        tv_category.setText(lost.getCategory());
        tv_color.setText(lost.getColor());
        if(lost.getBrand() != null && !lost.getBrand().isEmpty()){
            tv_brand.setText(lost.getBrand());
        }else{
            tv_brand.setVisibility(View.GONE);
        }

        if(lost.getBuilding() != null && !lost.getBuilding().isEmpty()){
            tv_building.setText(lost.getBuilding());
        }else{
            tv_building.setVisibility(View.GONE);
        }

        if(lost.getTags() != null && !lost.getTags().isEmpty()){
            tv_tags.setText(lost.getTags());
        }else{
            tv_tags.setVisibility(View.GONE);
        }

        tv_date.setText(AdditionalFunc.getDateString(lost.getRegisteredDate()));


        tv_rgt_name = (TextView)findViewById(R.id.tv_rgt_name);
        tv_rgt_studentId = (TextView)findViewById(R.id.tv_rgt_student_id);
        tv_rgt_phone = (TextView)findViewById(R.id.tv_rgt_phone);

        tv_rgt_name.setText(lost.getRgtUser().getName());
        tv_rgt_studentId.setText(lost.getRgtUser().getStudentId());
        tv_rgt_phone.setText(lost.getRgtUser().getPhone());

        tv_msg = (TextView)findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        li_request = (LinearLayout)findViewById(R.id.li_request);

        getRequestList();

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


    private void getRequestList(){

        HashMap<String, String> map = new HashMap<>();
        map.put("service", "inquiry_request_user");
        map.put("lost_id", lost.getId());

        new ParsePHP(Information.MAIN_SERVER_ADDRESS, map) {

            @Override
            protected void afterThreadFinish(String data) {

                requests = DalRequest.getRequestList(data);
                handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_MAKE_LIST));
//                if(requests.size() > 0) {
//                    handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_MAKE_LIST));
//                }else{
//                    handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_MAKE_LIST));
//                }

            }
        }.start();

    }

    public void makeList(){

        loading.setVisibility(View.GONE);

        li_request.removeAllViews();

        for(int i=0; i<requests.size(); i++){
            final DalRequest request  = requests.get(i);

            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_info_list_custom_item, null, false);

            TextView tv_name = (TextView)v.findViewById(R.id.tv_name);
            TextView tv_student_id = (TextView)v.findViewById(R.id.tv_student_id);
            TextView tv_phone = (TextView)v.findViewById(R.id.tv_phone);

            tv_name.setText(request.getRequestUser().getName());
            tv_student_id.setText(request.getRequestUser().getStudentId());
            tv_phone.setText(request.getRequestUser().getPhone());


            li_request.addView(v);

        }

        if (requests.size() > 0){
            tv_msg.setVisibility(View.GONE);
        }else{
            tv_msg.setVisibility(View.VISIBLE);
        }

    }


    private class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MESSAGE_SUCCESS:
                    progressDialog.hide();
                    new MaterialDialog.Builder(LostDetailActivity.this)
                            .title(R.string.success_srt)
                            .content("성공적으로 요청하였습니다.")
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LostDetailActivity.this.finish();
                                }
                            })
                            .show();
                    break;
                case MSG_MESSAGE_FAIL:
                    progressDialog.hide();
                    new MaterialDialog.Builder(LostDetailActivity.this)
                            .title(R.string.fail_srt)
                            .positiveText(R.string.ok)
                            .show();
                    break;
                case MSG_MESSAGE_MAKE_LIST:
                    makeList();
                    break;
                default:
                    break;
            }
        }
    }

    public String getImgUrl(String category) {

        String url;

        switch (category){
            case "mouse":
            case "마우스":
                url = "http://nearby.cf/dal_category/wallet.jpg";
                break;
            case "tumbler":
            case "텀블러":
                url = "http://nearby.cf/dal_category/tumbler.jpg";
                break;
            case "laptop":
            case "노트북":
                url = "http://nearby.cf/dal_category/laptop.jpg";
                break;
            case "wallet":
            case "지갑":
                url = "http://nearby.cf/dal_category/wallet.jpg";
                break;
            case "watch":
            case "손목시계":
                url = "http://nearby.cf/dal_category/watch.jpg";
                break;
            default:
                url = "http://nearby.cf/dal_category/default.jpg";
                break;
        }
        return url;
    }
}
