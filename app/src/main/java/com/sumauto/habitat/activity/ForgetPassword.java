package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.sumauto.common.util.ToastUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;

import org.json.JSONException;

public class ForgetPassword extends AppCompatActivity {
    EditText edit_phone, edit_sms_code, edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_sms_code = (EditText) findViewById(R.id.edit_sms_code);
        edit_password = (EditText) findViewById(R.id.edit_password);
    }

    public void onFindPasswordClick(View view) {
        String phone = edit_phone.getText().toString().trim();
        String code = edit_sms_code.getText().toString().trim();
        String pwd = edit_password.getText().toString().trim();
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        requestParams.put("code", code);
        requestParams.put("newuserpwd", pwd);
        HttpManager.getInstance().postApi(this, HttpManager.setNewPwd, requestParams, new HttpHandler() {
            @Override
            public void onSuccess(HttpResponse response) throws JSONException {
                ToastUtil.toast(response.msg);
                finish();
            }
        });
    }

    public void onSmsButtonClick(View v) {
        String phone = edit_phone.getText().toString().trim();
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        HttpManager.getInstance().postApi(this, HttpManager.getValidateCode, requestParams, new HttpHandler() {
            @Override
            public void onSuccess(HttpResponse response) throws JSONException {
                ToastUtil.toast(response.msg);
            }
        });
    }

}
