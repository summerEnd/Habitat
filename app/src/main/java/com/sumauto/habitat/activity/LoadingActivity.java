package com.sumauto.habitat.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.http.SyncHttpHandler;
import com.sumauto.habitat.utils.CityDB;
import com.sumauto.habitat.utils.Constant;

/**
 * Created by Lincoln on 16/4/2.
 * 加载
 */
public class LoadingActivity extends BaseActivity {

    private static final String TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.loading);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(imageView);
        showLoadingDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                Log.d("screen", dm.toString());
                initDB();
                doLogin();
                onLoadFinished();
            }
        }).start();
    }

    private void onLoadFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissLoadingDialog();
                Log.d(TAG, "onLoadFinished");
                if (HabitatApp.getInstance().isLogin()) {
                    to(MainActivity.class);
                } else {
                    // to(LoginActivity.class);
                    to(MainActivity.class);
                }
                finish();
                overridePendingTransition(R.anim.loading_activity_anim_in, R.anim.loading_activity_anim_out);
            }
        });
    }

    private void doLogin() {
        Log.d(TAG, "doLogin");
        HabitatApp habitatApp = getApplicationContext();
        Account account = habitatApp.getLoginAccount();
        if (account != null) {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            final String phone = manager.getUserData(account, HabitatApp.ACCOUNT_PHONE);
            final String password = manager.getPassword(account);

            HttpRequest<User> request = Requests.getLogin(phone, password);
            HttpManager.getInstance().postSync(LoadingActivity.this, new SyncHttpHandler<User>(request) {

                @Override
                public void onSuccess(HttpResponse response, HttpRequest<User> request, User user) {
                    getApplicationContext().login(password, user.toBundle());
                }

                @Override
                public void onShowMessage(HttpResponse response) {
                }
            });
        }
    }

    void initDB() {
        Log.d(TAG, "initDB");
        CityDB db = new CityDB(this);
        db.getWritableDatabase();
        db.close();
    }
}
