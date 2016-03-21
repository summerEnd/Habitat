package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sumauto.common.util.ActivityDismissUtils;
import com.sumauto.habitat.R;

public class SecondActivity extends AppCompatActivity {


    private ActivityDismissUtils.SwipeDismiss swipeDismiss;
    int visibility=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        swipeDismiss = ActivityDismissUtils.getSwipeDismiss(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSystemUiVisibility(visibility);
            }
        });

        a(R.id.SYSTEM_UI_FLAG_LOW_PROFILE);
        a(R.id.SYSTEM_UI_FLAG_FULLSCREEN);
        a(R.id.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        a(R.id.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        a(R.id.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        a(R.id.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    void a(int id) {
        CheckBox cb = (CheckBox) findViewById(id);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int flag=0;
                switch (buttonView.getId()) {
                    case R.id.SYSTEM_UI_FLAG_LOW_PROFILE:
                        flag=View.SYSTEM_UI_FLAG_LOW_PROFILE;
                        break;
                    case R.id.SYSTEM_UI_FLAG_FULLSCREEN:
                        flag=View.SYSTEM_UI_FLAG_FULLSCREEN;
                        break;
                    case R.id.SYSTEM_UI_FLAG_LAYOUT_STABLE:
                        flag=View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                        break;
                    case R.id.SYSTEM_UI_FLAG_IMMERSIVE_STICKY:
                        flag=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

                        break;
                    case R.id.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION:
                        flag=View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

                        break;
                    case R.id.SYSTEM_UI_FLAG_HIDE_NAVIGATION:
                        flag=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                        break;
                }
                if (isChecked)visibility|=flag;
                else visibility^=flag;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        swipeDismiss.process(ev);
        return super.dispatchTouchEvent(ev);
    }

}
