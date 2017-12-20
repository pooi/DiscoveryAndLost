package ga.discoveryandlost.discoveryandlost.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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

import ga.discoveryandlost.discoveryandlost.BaseActivity;
import ga.discoveryandlost.discoveryandlost.CustomApplication;
import ga.discoveryandlost.discoveryandlost.Information;
import ga.discoveryandlost.discoveryandlost.R;
import ga.discoveryandlost.discoveryandlost.StartActivity;
import ga.discoveryandlost.discoveryandlost.obj.DalItem;
import ga.discoveryandlost.discoveryandlost.obj.DalLost;
import ga.discoveryandlost.discoveryandlost.obj.DalSell;

public class RequestSellActivity extends BaseActivity {

    private MyHandler handler = new MyHandler();
    private final int MSG_MESSAGE_SUCCESS = 500;
    private final int MSG_MESSAGE_FAIL = 501;

    DalSell sell;

    private RoundedImageView img;
    private TextView tv_category;
    private TextView tv_color;
    private TextView tv_brand;
    private TextView tv_tags;
    private TextView tv_date;
    private TextView tv_price;

    private Button closeBtn;
    private Button buyBtn;

    private MaterialDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sell);

        Intent intent = getIntent();
        sell = (DalSell) intent.getSerializableExtra("sell");

        init();

    }

    private void init(){

        initProgressDialog();

        img = (RoundedImageView)findViewById(R.id.img);
        tv_category = (TextView)findViewById(R.id.tv_category);
        tv_color = (TextView)findViewById(R.id.tv_color);
        tv_brand = (TextView)findViewById(R.id.tv_brand);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_tags = (TextView)findViewById(R.id.tv_tags);
        tv_price = (TextView)findViewById(R.id.tv_price);
        closeBtn = (Button)findViewById(R.id.btn_close);
        buyBtn = (Button)findViewById(R.id.btn_buy);

        Picasso.with(getApplicationContext())
                .load(sell.getLost().getPhotos())
                .into(img);
        tv_category.setText(sell.getLost().getCategory());
        tv_color.setText(sell.getLost().getColor());
        if(sell.getLost().getBrand() != null && !sell.getLost().getBrand().isEmpty()){
            tv_brand.setText(sell.getLost().getBrand());
        }else{
            tv_brand.setVisibility(View.GONE);
        }

        if(sell.getLost().getTags() != null && !sell.getLost().getTags().isEmpty()){
            tv_tags.setText(sell.getLost().getTags());
        }else{
            tv_tags.setVisibility(View.GONE);
        }

        tv_date.setVisibility(View.GONE);
        tv_price.setText("가격 : " + sell.getPrice().toString() + "원");

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestSellActivity.this.finish();
            }
        });
        buyBtn.setOnClickListener(new View.OnClickListener() {
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



    }

    private class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MESSAGE_SUCCESS:
                    progressDialog.hide();
                    new MaterialDialog.Builder(RequestSellActivity.this)
                            .title(R.string.success_srt)
                            .content("성공적으로 요청하였습니다.")
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    RequestSellActivity.this.finish();
                                }
                            })
                            .show();
                    break;
                case MSG_MESSAGE_FAIL:
                    progressDialog.hide();
                    new MaterialDialog.Builder(RequestSellActivity.this)
                            .title(R.string.fail_srt)
                            .positiveText(R.string.ok)
                            .show();
                    break;
                default:
                    break;
            }
        }
    }

}
