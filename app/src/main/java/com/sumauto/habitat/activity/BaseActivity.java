package com.sumauto.habitat.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sumauto.common.SystemStatusManager;
import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/21.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTranslucentStatus();
    }

    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus()
    {

        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.titleBarBackground);//状态栏无背景
    }
}
