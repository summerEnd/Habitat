package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.sumauto.habitat.BuildConfig;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;

/**
 * Created by Lincoln on 16/4/2.
 * 加载
 */
public class LoadingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.loading);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(imageView);
        to(MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //enter();
            }
        }, 0);
    }

    void enter() {
        String account;
        String password;

        if (BuildConfig.USE_DEMO_USER){
            account="18652947363";
            password="123456";
        }else{
            account = HabitatApp.getInstance().getAccount();
            password = HabitatApp.getInstance().getPassword();
        }

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            to(LoginActivity.class);
            finish();
            overridePendingTransition(R.anim.loading_activity_anim_in, R.anim.loading_activity_anim_out);
        } else {
            HttpRequest<User> request = Requests.getLogin(account, password);
            HttpManager.getInstance().post(this, new JsonHttpHandler<User>(request) {

                @Override
                public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
                    HabitatApp.getInstance().setUser(user);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    to(MainActivity.class);
                    finish();
                    overridePendingTransition(R.anim.loading_activity_anim_in, R.anim.loading_activity_anim_out);
                }
            });

        }
    }
}
