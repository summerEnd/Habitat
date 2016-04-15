package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.mipmap.loading);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(imageView);
        enter();
    }

    void enter(){
        String account=HabitatApp.getInstance().getAccount();
        String password=HabitatApp.getInstance().getPassword();
        if (TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
            to(LoginActivity.class);
            finish();
        }else{
            HttpRequest<User> request= Requests.getLogin(account,password);
            HttpManager.getInstance().post(this, new JsonHttpHandler<User>(request) {

                @Override
                public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
                    HabitatApp.getInstance().setUser(user);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            to(MainActivity.class);
                            finish();
                        }
                    },1000);
                }
            });

        }

    }
}
