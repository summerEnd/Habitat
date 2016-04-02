package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.common.SystemStatusManager;
import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/21.
 * 基类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected String TAG = getClass().getSimpleName();
    private SystemStatusManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tintManager = new SystemStatusManager(this);

        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.drawable.titleBarBackground);
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
                tv_title.setText(getTitle());
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
        initToolBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initToolBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initToolBar();
    }
}
