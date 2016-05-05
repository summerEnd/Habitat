package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.callback.SimpleUMListener;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.util.SLog;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class LoginActivity extends BaseActivity {
    private EditText edit_phone;
    private EditText edit_password;
    private UMShareAPI mUMShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
        mUMShareAPI = UMShareAPI.get(this);
    }

    //登录
    public void onLoginClick(View v) {
        final String phone = edit_phone.getText().toString();
        final String pwd = edit_password.getText().toString();
        showLoadingDialog();
        HttpManager.getInstance().post(this, new JsonHttpHandler<User>(Requests.getLogin(phone, pwd)) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
                to(MainActivity.class);
                HabitatApp.getInstance().login(pwd,user.toBundle());
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissLoadingDialog();
            }
        });

    }

    //忘记密码
    public void onForgetPasswordClick(View v) {
        to(ForgetPassword.class);
    }

    //注册
    public void onRegisterClick(View v) {
        to(RegisterActivity.class);
    }

    //微信点击
    public void onWeiXinClick(View v) {
        mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new SimpleUMListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                SLog.d("SHARE_MEDIA:%s i:%d ", share_media.toString(), i);
            }
        });
    }

    //qq点击
    public void onQQClick(View v) {
        mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, new SimpleUMListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                SLog.d("SHARE_MEDIA:%s i:%d ", share_media.toString(), i);
            }
        });
    }

    //微博点击
    public void onWeiboClick(View v) {
        mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, new SimpleUMListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                SLog.d("SHARE_MEDIA:%s i:%d ", share_media.toString(), i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUMShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
