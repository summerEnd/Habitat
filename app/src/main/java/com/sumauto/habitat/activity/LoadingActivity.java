package com.sumauto.habitat.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.Constant;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enter();
            }
        }, 0);
        showLoadingDialog();

    }

    void enter() {
        HabitatApp habitatApp = getApplicationContext();
        Account account = habitatApp.getLoginAccount();
        if (account != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            final String phone = manager.getUserData(account, HabitatApp.ACCOUNT_PHONE);
            final String password = manager.getPassword(account);

            HttpRequest<User> request = Requests.getLogin(phone, password);
            HttpManager.getInstance().post(this, new JsonHttpHandler<User>(request) {

                @Override
                public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
                    Bundle b = new Bundle();
                    getApplicationContext().login(password,user.toBundle());
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    dismissLoadingDialog();
                    to(MainActivity.class);
                    finish();
                    overridePendingTransition(R.anim.loading_activity_anim_in, R.anim.loading_activity_anim_out);
                }
            });
        }else{
            to(LoginActivity.class);
            finish();
            overridePendingTransition(R.anim.loading_activity_anim_in, R.anim.loading_activity_anim_out);
        }

    }
}
