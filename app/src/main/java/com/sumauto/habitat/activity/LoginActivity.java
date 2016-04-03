package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity {
    private EditText edit_phone;
    private EditText edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
    }


    //登录
    public void onLoginClick(View v){
        final String phone = edit_phone.getText().toString();
        final String pwd = edit_password.getText().toString();

        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        requestParams.put("pwd", pwd);
        HttpManager.getInstance().postApi(this, HttpManager.getLogin, requestParams, new HttpHandler() {
            @Override
            public void onSuccess(HttpResponse response) throws JSONException {
                User user = new User().initWith(new JSONObject(response.data).getJSONObject("login"));
                ((HabitatApp) getApplication()).setUser(user);
                to(MainActivity.class);
                finish();
            }
        });
    }

    //忘记密码
    public void onForgetPasswordClick(View v){
        to(ForgetPassword.class);
    }
    //注册
    public void onRegisterClick(View v){}

    //微信点击
    public void onWeiXinClick(View v) {
    }

    //qq点击
    public void onQQClick(View v) {
    }

    //微博点击
    public void onWeiboClick(View v) {
    }
}
