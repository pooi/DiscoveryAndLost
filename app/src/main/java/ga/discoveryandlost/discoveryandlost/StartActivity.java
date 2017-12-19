package ga.discoveryandlost.discoveryandlost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import ga.discoveryandlost.discoveryandlost.obj.User;
import ga.discoveryandlost.discoveryandlost.util.ParsePHP;


public class StartActivity extends BaseActivity {

    public static User USER;

    private MyHandler handler = new MyHandler();
    private final int MSG_MESSAGE_SUCCESS = 500;
    private final int MSG_MESSAGE_FAIL = 501;
    private final int MSG_MESSAGE_CHECK_LOGIN = 502;

    // UI
    private KenBurnsView kenBurnsView;
    private RelativeLayout rl_background;

    private LinearLayout formLogin;
    private MaterialEditText formId;
    private MaterialEditText formPw;
    private Button formLoginBtn;

    private MaterialDialog progressDialog;

    // Auto Login
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {

            finish();

        }else{

            setting = getSharedPreferences("setting", 0);
            editor = setting.edit();

            init();

            new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                @Override
                public void run() {

                    handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_CHECK_LOGIN));

                }
            }, 500);

        }

    }

    private void init(){

        kenBurnsView = (KenBurnsView)findViewById(R.id.image);
        kenBurnsView.setImageResource(R.drawable.loading);

        rl_background = (RelativeLayout)findViewById(R.id.rl_background);

        initLoginForm();

//        tv_signupSupporterBtn = (TextView)findViewById(R.id.tv_signup_supporter);
//        tv_signupSupporterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StartActivity.this, RegisterSupporterActivity.class);
//                startActivity(intent);
//            }
//        });

        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .theme(Theme.LIGHT)
                .build();

        findViewById(R.id.rl_background).setVisibility(View.GONE);

    }

    private void initLoginForm(){

        formLogin = (LinearLayout)findViewById(R.id.form_login);
        formId = (MaterialEditText)findViewById(R.id.form_id);
        formPw = (MaterialEditText)findViewById(R.id.form_pw);
        formLoginBtn = (Button)findViewById(R.id.form_login_btn);
        formLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkLoginBtn();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        formId.addTextChangedListener(textWatcher);
        formPw.addTextChangedListener(textWatcher);

    }

    private void checkAlreadyLogin(){

        String loginId = setting.getString("login_id", null);
        String loginPw = setting.getString("login_pw", null);


        if(loginId != null && loginPw != null){

            login(loginId, loginPw);

        }else{

            findViewById(R.id.rl_background).setVisibility(View.VISIBLE);
            setFadeInAnimation(findViewById(R.id.rl_background));

        }

    }

    private void checkLoginBtn(){

        boolean isInputId = !formId.getText().toString().isEmpty();
        boolean isInputPw = !formPw.getText().toString().isEmpty();

        boolean status = isInputId && isInputPw;

        formLoginBtn.setEnabled(status);

        if(status){
            formLoginBtn.setBackgroundColor(getColorId(R.color.colorPrimary));
        }else{
            formLoginBtn.setBackgroundColor(getColorId(R.color.dark_gray));
        }

    }

    private class MyHandler extends Handler implements Serializable {

        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_MESSAGE_SUCCESS:
                    progressDialog.hide();
                    break;
                case MSG_MESSAGE_FAIL:
                    progressDialog.hide();
                    new MaterialDialog.Builder(StartActivity.this)
                            .title(R.string.fail_srt)
                            .content(getString(R.string.login_failed_srt) + "\n" + getString(R.string.please_check_id_or_pw))
                            .positiveText(R.string.ok)
                            .show();
                    formPw.setText("");
                    findViewById(R.id.rl_background).setVisibility(View.VISIBLE);
                    break;
                case MSG_MESSAGE_CHECK_LOGIN:
                    checkAlreadyLogin();
                    break;
                default:
                    break;
            }
        }
    }

    private void login(){

        if(formId.getText().toString().length() <= 0 && formPw.getText().toString().length() <= 0){
            showSnackbar(R.string.please_enter_id_and_pw);
            return;
        }else if(formId.getText().toString().length() <= 0){
            showSnackbar(R.string.please_enter_id);
            return;
        }else if(formPw.getText().toString().length() <= 0){
            showSnackbar(R.string.please_enter_pw);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("service", "login_user");
        map.put("login_id", formId.getText().toString());
        map.put("login_pw", formPw.getText().toString());

        progressDialog.show();

        new ParsePHP(Information.MAIN_SERVER_ADDRESS, map){
            @Override
            protected void afterThreadFinish(String data) {

                editor.putString("login_id", formId.getText().toString());
                editor.putString("login_pw", formPw.getText().toString());
                editor.commit();

                User user = new User(data);

                if(!user.isEmpty()){
                    USER = user;
                    handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_SUCCESS));

                    redirectMainActivity();

                }else{
                    handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_FAIL));
                }



            }
        }.start();

    }

    private void login(String id, String pw){

        HashMap<String, String> map = new HashMap<>();
        map.put("service", "login_user");
        map.put("login_id", id);
        map.put("login_pw", pw);

        progressDialog.show();

        new ParsePHP(Information.MAIN_SERVER_ADDRESS, map){
            @Override
            protected void afterThreadFinish(String data) {

                User user = new User(data);

                USER = user;
                handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_SUCCESS));
                redirectMainActivity();

            }
        }.start();

    }



    private void redirectMainActivity(){
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public static void initLoginData(){
        USER = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }



}
