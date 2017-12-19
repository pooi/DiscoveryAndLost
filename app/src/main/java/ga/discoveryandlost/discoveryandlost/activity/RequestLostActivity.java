package ga.discoveryandlost.discoveryandlost.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.CustomApplication;
import ga.discoveryandlost.discoveryandlost.Information;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.StartActivity;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.obj.DalLost;

public class RequestLostActivity extends BaseActivity {

    private MyHandler handler = new MyHandler();
    private final int MSG_MESSAGE_SUCCESS = 500;
    private final int MSG_MESSAGE_FAIL = 501;

    DalItem item;
    DalLost lost;

    private RoundedImageView img;
    private TextView tv_category;
    private TextView tv_color;
    private TextView tv_brand;
    private TextView tv_building;
    private TextView tv_tags;
    private TextView tv_date;
    private TextView tv_match;
    private TextView tv_msg;

    private Button cancelBtn;
    private Button requestBtn;

    private MaterialDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_lost);

        Intent intent = getIntent();
        item = (DalItem)intent.getSerializableExtra("item");
        lost = (DalLost)intent.getSerializableExtra("lost");

        init();

    }

    private void init(){

        initProgressDialog();

        img = (RoundedImageView)findViewById(R.id.img);
        tv_category = (TextView)findViewById(R.id.tv_category);
        tv_color = (TextView)findViewById(R.id.tv_color);
        tv_brand = (TextView)findViewById(R.id.tv_brand);
        tv_building = (TextView)findViewById(R.id.tv_building);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_tags = (TextView)findViewById(R.id.tv_tags);
        tv_match = (TextView)findViewById(R.id.tv_match);
        cancelBtn = (Button)findViewById(R.id.btn_cancel);
        requestBtn = (Button)findViewById(R.id.btn_request);
        tv_msg = (TextView)findViewById(R.id.tv_msg);

        Picasso.with(getApplicationContext())
                .load(getImgUrl(item.getCategory()))
                .into(img);
        tv_category.setText(item.getCategory());
        tv_color.setText(item.getColor());
        if(item.getBrand() != null && !item.getBrand().isEmpty()){
            tv_brand.setText(item.getBrand());
        }else{
            tv_brand.setVisibility(View.GONE);
        }

        if(item.getBuilding() != null && !item.getBuilding().isEmpty()){
            tv_building.setText(item.getBuilding());
        }else{
            tv_building.setVisibility(View.GONE);
        }

        if(item.getTags() != null && !item.getTags().isEmpty()){
            tv_tags.setText(item.getTags());
        }else{
            tv_tags.setVisibility(View.GONE);
        }

        tv_date.setVisibility(View.GONE);
        tv_match.setText("69%");

        if(lost.getMatch() < 70.0){
            tv_msg.setVisibility(View.VISIBLE);
        }else{
            tv_msg.setVisibility(View.GONE);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestLostActivity.this.finish();
            }
        });
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
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

    private void request(){

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

        smr.addStringParam("service", "Request");

        smr.addStringParam("lost_id", lost.getId());
        smr.addStringParam("user_id", StartActivity.USER.getId());
        smr.addStringParam("category", item.getCategory() == null ? "" : item.getCategory());
        smr.addStringParam("color", item.getColor() == null ? "" : item.getColor());
        smr.addStringParam("brand", item.getBrand() == null ? "" : item.getBrand());
        smr.addStringParam("building", item.getBuilding() == null ? "" : item.getBuilding());
        smr.addStringParam("room", item.getRoom() == null ? "" : item.getRoom());
        smr.addStringParam("tags", item.getTags() == null ? "" : item.getTags());
        smr.addStringParam("match", "70");


        CustomApplication.getInstance().addToRequestQueue(smr);

    }

    private class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MESSAGE_SUCCESS:
                    progressDialog.hide();
                    new MaterialDialog.Builder(RequestLostActivity.this)
                            .title(R.string.success_srt)
                            .content("성공적으로 요청하였습니다.")
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    RequestLostActivity.this.finish();
                                }
                            })
                            .show();
                    break;
                case MSG_MESSAGE_FAIL:
                    progressDialog.hide();
                    new MaterialDialog.Builder(RequestLostActivity.this)
                            .title(R.string.fail_srt)
                            .positiveText(R.string.ok)
                            .show();
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
