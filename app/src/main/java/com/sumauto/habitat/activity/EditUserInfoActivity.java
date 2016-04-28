package com.sumauto.habitat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.util.ImageUtil;
import com.sumauto.util.ToastUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.widget.IosListDialog;

import java.io.IOException;

public class EditUserInfoActivity extends BaseActivity {
    public static final int REQUEST_CODE = 1;
    TextView tv_gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compete_data);
        tv_gender= (TextView) findViewById(R.id.tv_gender);
    }

    public void onAvatarClick(View v){
        ToastUtil.toast("avatar");
        PhotoActivity.start(this,300,300, REQUEST_CODE);
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

    public void onSaveClick(View view) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&REQUEST_CODE==requestCode){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                HttpRequest<String> request = Requests.setHeadImg(ImageUtil.base64Encode(bitmap));

                HttpManager.getInstance().post(this, new JsonHttpHandler<String>(request) {
                    @Override
                    public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
