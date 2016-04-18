package com.sumauto.habitat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sumauto.util.ToastUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.widget.IosListDialog;

public class EditUserInfoActivity extends BaseActivity {
    TextView tv_gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compete_data);
        tv_gender= (TextView) findViewById(R.id.tv_gender);
    }

    public void onAvatarClick(View v){
        ToastUtil.toast("avatar");
        startActivity(new Intent(this,PhotoActivity.class));
    }

    public void onGenderClick(View v) {
        ToastUtil.toast("onGenderClick");
        new IosListDialog(this).listener(new IosListDialog.Listener() {
            @Override
            public void onClick(IosListDialog dialog, int position) {
                if (position==0){
                    tv_gender.setText("男");
                }else{
                    tv_gender.setText("女");
                }
            }
        }).show("男", "女");

    }

    public void onBirthdayClick(View v) {
        ToastUtil.toast("onBirthdayClick");

    }
}