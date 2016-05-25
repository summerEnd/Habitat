package com.sumauto.habitat.activity;

import android.app.Activity;
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
                onUserLoginComplete(user, pwd);
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
        mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new ThirdLoginHandler(this));
    }

    //qq点击
    public void onQQClick(View v) {
        mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, new ThirdLoginHandler(this));
    }

    //微博点击
    public void onWeiboClick(View v) {
        mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, new ThirdLoginHandler(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUMShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    private class ThirdLoginHandler extends SimpleUMListener {
        Activity activity;

        public ThirdLoginHandler(Activity activity) {
            this.activity = activity;
        }


        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            mUMShareAPI.getPlatformInfo(activity, share_media, new SimpleUMListener() {

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    String nickname = "";
                    String headimg = "";
                    String snsaccount = "";
                    switch (share_media) {
                        case QQ:
                        case QZONE: {

                            nickname = map.get("screen_name");
                            headimg = map.get("profile_image_url");
                            snsaccount = map.get("openid");

                            //nickname="Lincoln";
                            //headimg="http://q.qlogo.cn/qqapp/1105345733/8B90406F4D7A491493D6A82F606BF6A9/100";
                            //snsaccount="8B90406F4D7A491493D6A82F606BF6A9";
                            break;
                        }
                        case WEIXIN:
                        case WEIXIN_CIRCLE: {
                            //nickname = map.get("screen_name");
                            //headimg = map.get("profile_image_url");
                            //snsaccount = map.get("openid");
                            break;
                        }
                        case SINA: {
                            //nickname = map.get("screen_name");
                            //headimg = map.get("profile_image_url");
                            //snsaccount = map.get("openid");
                            break;
                        }

                    }

                    //进行第三方登录
                    HttpRequest<User> request = Requests.getThirdLogin(nickname, headimg, snsaccount);

                    HttpManager.getInstance().post(activity, new JsonHttpHandler<User>(request) {
                        @Override
                        public void onSuccess(HttpResponse response, HttpRequest<User> request, User bean) {
                            onUserLoginComplete(bean, "123");
                        }
                    });
                }
            });
        }
    }

    private void onUserLoginComplete(User user, String pwd) {
        to(MainActivity.class);
        HabitatApp.getInstance().login(pwd, user.toBundle());
        finish();
    }
}
