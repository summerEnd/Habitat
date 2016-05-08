package com.sumauto.habitat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.common.SystemStatusManager;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.callback.OnActivityResultCallback;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.widget.LoadingDialog;

import java.lang.reflect.Field;

/**
 * Created by Lincoln on 16/3/21.
 * 基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected String TAG = getClass().getSimpleName();
    private SystemStatusManager tintManager;
    private OnActivityResultCallback mActivityResultCallback;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tintManager = new SystemStatusManager(this);

        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.titleBarBackground);
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        mLoadingDialog.show(getSupportFragmentManager(), "load");
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
    }

    public SystemStatusManager getTintManager() {
        return tintManager;
    }

    protected void initToolBar() {
        initToolBar(R.id.toolBar);
    }

    protected void initToolBar(int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //返回按钮
            View back = toolbar.findViewById(R.id.toolBar_back);
            if (back != null) {
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            //标题
            TextView tv_title = (TextView) toolbar.findViewById(R.id.toolBar_title);
            if (tv_title != null) {
                //tv_title.setText(getTitle());
            }
        }
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        init();
    }

    private void init() {
        initToolBar();
        Field[] declaredFields = getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            ViewId annotation = field.getAnnotation(ViewId.class);
            if (annotation == null) continue;
            int id = annotation.value();
            if (id==0){
                String fieldName=field.getName();
                 id = getResources().getIdentifier(fieldName, "id", getPackageName());
            }
            try {
                field.set(this, findViewById(id));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void to(Class<? extends Activity> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    public void setActivityResultCallback(OnActivityResultCallback mActivityResultCallback) {
        this.mActivityResultCallback = mActivityResultCallback;
    }

    protected String string(TextView textView){
        return textView.getText().toString().trim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mActivityResultCallback != null) {
            mActivityResultCallback.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public HabitatApp getApplicationContext() {
        return (HabitatApp) super.getApplicationContext();
    }

    public String getUserData(String key){
        return getApplicationContext().getUserData(key);
    }
}
