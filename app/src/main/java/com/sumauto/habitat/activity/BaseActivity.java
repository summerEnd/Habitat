package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sumauto.common.SystemStatusManager;
import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/21.
 * 基类
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
    private void setTranslucentStatus() {

        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.titleBarBackground);//状态栏无背景
    }

    protected void initToolBar(int  toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.findViewById(R.id.toolBar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_title= (TextView) toolbar.findViewById(R.id.toolBar_title);
        tv_title.setText(getTitle());
    }
}
