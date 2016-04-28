package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.util.FileUtil;

public class SettingActivity extends BaseActivity {

    @ViewId(R.id.tv_cache_size)TextView mTvCacheSize;

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
    }

    public void onInviteFriends(View view) {
    }

    public void onBlackListClick(View view) {
    }

    public void onClearCache(View view) {
        FileUtil.delete(getCacheDir());
        setCacheSize();
    }


    private void setCacheSize() {
        mTvCacheSize.setText(Formatter.formatFileSize(this, FileUtil.getSize(getCacheDir())));
    }
}
