package com.sumauto.habitat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.Map;

public class LoginActivity extends BaseActivity {
    private EditText edit_phone;
    private EditText edit_password;
    private com.umeng.socialize.controller.UMSocialService mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
        initData();
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
    private void initData()
    {
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        UMQQSsoHandler qqSsoHandler =
                new UMQQSsoHandler(LoginActivity.this, "1105345733", "0nodokOURqi71RPx");
        qqSsoHandler.addToSocialSDK();

        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        UMWXHandler wxHandler = new UMWXHandler(LoginActivity.this,"wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        wxHandler.addToSocialSDK();

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
        mController.doOauthVerify(this, SHARE_MEDIA.WEIXIN, new ThirdLoginHandler(this));
    }

    //qq点击
    public void onQQClick(View v) {
        mController.doOauthVerify(this, SHARE_MEDIA.QQ, new ThirdLoginHandler(this));
    }

    //微博点击
    public void onWeiboClick(View v) {
        mController.doOauthVerify(this, SHARE_MEDIA.SINA, new ThirdLoginHandler(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null)
        {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private class ThirdLoginHandler extends SimpleUMListener {
        Activity activity;

        public ThirdLoginHandler(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onComplete(Bundle bundle, final SHARE_MEDIA share_media) {
            super.onComplete(bundle, share_media);
            mController.getPlatformInfo(activity, share_media, new SimpleUMListener() {

                @Override
                public void onComplete(int i, Map<String, Object> map)  {
                    String nickname = "";
                    String headimg = "";
                    String snsaccount = "";
                    switch (share_media) {
                        case QQ:
                        case QZONE: {

                            nickname = (String) map.get("screen_name");
                            headimg = (String) map.get("profile_image_url");
                            snsaccount = (String) map.get("openid");

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

                            //"followers_count" -> "90"
                            //profile_image_url" -> "http://tva1.sinaimg.cn/crop.3.0.458.458.180/005Cv8FBgw1egsx5l3nalj30cu0csabp.jpg"
                            //description" -> "寻求快乐"
                            //screen_name" -> "夏尾秋初"
                            //location" -> "江苏 南京"
                            //access_token" -> "2.00bfIVcFzWTuhDb15167266b0TXJ5I"
                            //verified" -> "false"
                            //gender" -> "1"
                            //uid" -> "5149586427"
                            //favourites_count" -> "9"
                            //"statuses_count" -> "45"
                            //"friends_count" -> "56"
                            nickname = (String) map.get("screen_name");
                            headimg = (String) map.get("profile_image_url");
                            snsaccount = (String) map.get("access_token");
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
