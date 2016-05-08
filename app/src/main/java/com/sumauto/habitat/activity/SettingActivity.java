package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.util.FileUtil;
import com.sumauto.util.ToastUtil;

public class SettingActivity extends BaseActivity {

    @ViewId(R.id.tv_cache_size)TextView mTvCacheSize;
    @ViewId  SwitchCompat iv_switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setCacheSize();
    }


    public void onChangePassword(View view) {
        to(EditPasswordActivity.class);
    }

    public void onUpdateVersion(View view) {
        ToastUtil.toast(this,"当前是最新版本～");
    }

    public void onInviteFriends(View view) {
    }

    public void onBlackListClick(View view) {
        to(BlackNoteActivity.class);
    }

    public void onClearCache(View view) {
        FileUtil.delete(getCacheDir());
        setCacheSize();
    }


    private void setCacheSize() {
        mTvCacheSize.setText(Formatter.formatFileSize(this, FileUtil.getSize(getCacheDir())));
    }

    public void onCollect(View view) {
        CollectionsActivity.start(this,getUserData(HabitatApp.ACCOUNT_UID));
    }

    public void onMsgClick(View view) {

    }
}
