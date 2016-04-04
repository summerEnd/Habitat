package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.sumauto.habitat.R;

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              to(LoginActivity.class);
            }
        },2000);
    }
}
