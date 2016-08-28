package com.beautifish.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.beautifish.R;
import com.beautifish.app.App;
import com.beautifish.models.RequireResult;
import com.beautifish.utils.HttpAsyncTask;
import com.beautifish.widget.LoadingDialog;

public class LoginActivity extends BaseActivity implements View.OnClickListener, HttpAsyncTask.IHttpAsyncTask {

    private static final String TAG = LoginActivity.class.getName();
    private static final int REQUIRE_TYPE_LOGIN_HX = 0;//登录环信
    private static final int REQUIRE_TYPE_LOGIN_RF = 1;//登录富爸爸
    private int requireType;

    private AutoCompleteTextView tvUserName;
    private EditText editPassword;
    private Button btnLogin, btnRegister;

    private String password;
    private String phone;

    private RequireResult loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(R.layout.activity_login);
        tvUserName = (AutoCompleteTextView) findViewById(R.id.email);
        editPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        String phone = App.getData4SP(this, App.SP_PACKAGE_USER, App.SP_KEY_PHONE);
        if(!phone.equalsIgnoreCase("-1")){
            tvUserName.setText(phone);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(checkParams()){
                    HttpAsyncTask.getInstance(LoginActivity.this, REQUIRE_TYPE_LOGIN_RF);//登陆富爸爸
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    //验证参数合法性
    private boolean checkParams(){
        phone = tvUserName.getText()+"";
        password = editPassword.getText()+"";
        if (!App.isMobileNO(phone)){
            App.toast(this, getResources().getString(R.string.password_phone_err));
            return false;
        }
        if(password.length()<6){
            App.toast(this, getResources().getString(R.string.password_length_err));
            return false;
        }
        return true;
    }

    private static final int SHOW_PROGRESS_BAR = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_PROGRESS_BAR:
                    LoadingDialog.showDialog(LoginActivity.this);
                    break;
            }
        }
    };

    @Override
    public void onPreExecute() {
        handler.sendEmptyMessage(SHOW_PROGRESS_BAR);
    }

    @Override
    public Object doInBackground(Object... params) {
        requireType = (Integer)params[0];
        switch (requireType){
            case REQUIRE_TYPE_LOGIN_HX:
                break;
            case REQUIRE_TYPE_LOGIN_RF:
                loginResult = App.login(this, App.LOGIN,  phone, password);
                break;
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Integer... values) {

    }

    @Override
    public void onPostExecute(Object result) {
        LoadingDialog.dismissDialog();
        switch (requireType){
            case REQUIRE_TYPE_LOGIN_HX:
                break;
            case REQUIRE_TYPE_LOGIN_RF:
                if(loginResult == null){
                    return;
                }
                String status = loginResult.getStatus();
                App.saveData2SP(this, App.SP_PACKAGE_USER, App.SP_KEY_LOGIN_STATUS, status);
                if(status.equalsIgnoreCase("0")){
                    App.saveData2SP(this, App.SP_PACKAGE_USER, App.SP_KEY_PHONE, phone);
                    App.toast(this, loginResult.getMsg());
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    App.toast(this, loginResult.getMsg());
                }
                break;
        }
    }
}

