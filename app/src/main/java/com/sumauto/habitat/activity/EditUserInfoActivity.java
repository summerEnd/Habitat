package com.sumauto.habitat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.habitat.widget.IosListDialog;
import com.sumauto.util.FileUtil;
import com.sumauto.util.ImageUtil;
import com.sumauto.util.ToastUtil;
import com.sumauto.wheel.TimePickerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditUserInfoActivity extends BaseActivity {
    public static final int REQUEST_CODE = 1;
    @ViewId TextView tv_gender;
    @ViewId TextView tv_birthday;
    @ViewId ImageView iv_avatar;
    @ViewId ImageView iv_add_com;
    @ViewId EditText edit_nick;
    @ViewId EditText edit_commit_name;
    @ViewId EditText edit_signature;
    Bundle changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compete_data);

        changes = new Bundle();
        ImageLoader.getInstance().displayImage(
                getUserData(HabitatApp.ACCOUNT_AVATAR), iv_avatar, ImageOptions.options());
        tv_gender.setText(getUserData(HabitatApp.ACCOUNT_GENDER));
        tv_birthday.setText(getUserData(HabitatApp.ACCOUNT_BIRTHDAY));

        edit_nick.setText(getUserData(HabitatApp.ACCOUNT_NICK));
        edit_commit_name.setText(getUserData(HabitatApp.ACCOUNT_COMM_NAME));
        edit_signature.setText(getUserData(HabitatApp.ACCOUNT_SIGNATURE));

        edit_nick.addTextChangedListener(new ChangeWatcher(Requests.nickname));
        edit_signature.addTextChangedListener(new ChangeWatcher(Requests.signature));

        String commid = getUserData(HabitatApp.ACCOUNT_COMMID);
        if (TextUtils.isEmpty(commid)) {
            //没有社区
            iv_add_com.setVisibility(View.VISIBLE);
            edit_commit_name.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            edit_commit_name.setOnClickListener(null);
        } else {
            //有社区
            iv_add_com.setVisibility(View.GONE);
            edit_commit_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddComm(v);
                }
            });
            edit_commit_name.setInputType(EditorInfo.TYPE_NULL);

        }
    }

    //添加社区
    public void onAddComm(View view) {
        CreateCommunity.start(this,edit_commit_name.getText().toString());
    }

    private class ChangeWatcher implements TextWatcher {
        String key;

        public ChangeWatcher(String key) {
            this.key = key;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null) {
                changes.putString(key, s.toString());
            }
        }
    }

    public void onAvatarClick(View v) {
        PhotoActivity.Options options = new PhotoActivity.Options();
        options.outHeight = options.outWidth = 300;
        options.multiply = false;
        PhotoActivity.start(this, options, REQUEST_CODE);
    }

    public void onGenderClick(View v) {
        new IosListDialog(this).listener(new IosListDialog.Listener() {
            @Override
            public void onClick(IosListDialog dialog, int position) {
                String gender = position == 0 ? "男" : "女";
                tv_gender.setText(gender);
                changes.putString(Requests.sex, gender);
            }
        }).show("男", "女");
    }

    public void onBirthdayClick(View v) {

        TimePickerView timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setRange(1920, 2020);
        timePickerView.setCyclic(false);
        timePickerView.setTitle(getString(R.string.birthday));

        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                String birthday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
                changes.putString(Requests.birthday, birthday);
                tv_birthday.setText(birthday);
            }
        });
        timePickerView.show();
    }

    public void onSaveClick(View view) {

        try {
            HttpManager.getInstance().post(this, new JsonHttpHandler<User>(Requests.changeUserInfo(changes)) {
                @Override
                public void onSuccess(HttpResponse response, HttpRequest<User> request, User bean) {
                    ToastUtil.toast(EditUserInfoActivity.this, response.msg);
                    getApplicationContext().setUserData(bean.toBundle());
                    finish();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("","头像文件未找到");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && REQUEST_CODE == requestCode) {
            List<Uri> uris = PhotoActivity.handleResult(data);
            if (!uris.isEmpty()) {
                Uri uri = uris.get(0);
                iv_avatar.setImageURI(uri);
                try {
                    changes.putString(Requests.headimg, FileUtil.uriToFile(uri).getAbsolutePath());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
