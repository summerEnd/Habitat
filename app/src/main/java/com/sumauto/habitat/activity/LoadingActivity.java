package com.sumauto.habitat.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.BuildConfig;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.widget.LoadingDialog;

import org.json.JSONException;

import java.io.File;

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
        AccountManager manager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account("朱超", getString(R.string.account_type));
        manager.addAccountExplicitly(account, "123", null);
    }

    void enter() {
        String account = "";
        String password = "";
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] accountsByType = manager.getAccounts();
        if (accountsByType.length != 0) {
            for (int i = 0; i < accountsByType.length; i++) {
                //password=  manager.getPassword(accountsByType[0]);
                account = accountsByType[0].name;
                Log.d("tag", "name:" + account + " pwd:" + password);
            }
        }

        account = "18652947363";
        password = "1234567";

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            to(LoginActivity.class);
            finish();
            overridePendingTransition(R.anim.loading_activity_anim_in, R.anim.loading_activity_anim_out);
        } else {
            HttpRequest<User> request = Requests.getLogin(account, password);
            HttpManager.getInstance().post(this, new JsonHttpHandler<User>(request) {

                @Override
                public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
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
        }
    }
}
