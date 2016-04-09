package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;

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
        Requests.GetLogin request=new Requests.GetLogin(phone,pwd);
        HttpManager.getInstance().post(this, new JsonHttpHandler<User>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
                to(MainActivity.class);
                HabitatApp.getInstance().setPassword(pwd);
                HabitatApp.getInstance().setAccount(phone);
                finish();
            }
        });

    }

    //忘记密码
    public void onForgetPasswordClick(View v){
        //to(ForgetPassword.class);
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
