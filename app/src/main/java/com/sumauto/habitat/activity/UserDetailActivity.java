package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;

public class UserDetailActivity extends BaseActivity {
    @ViewId TextView tv_signature;
    @ViewId TextView tv_commit_name;
    @ViewId TextView tv_birthday;
    @ViewId TextView tv_gender;
    @ViewId TextView tv_nick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }
}
