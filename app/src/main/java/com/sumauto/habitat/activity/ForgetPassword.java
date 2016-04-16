package com.sumauto.habitat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.sumauto.util.ToastUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;

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

        HttpManager.getInstance().post(this, new JsonHttpHandler<User>(Requests.setNewPwd(phone, code, pwd)) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<User> request, User bean) {
                ToastUtil.toast(response.msg);
                finish();
            }
        });


    }

    public void onSmsButtonClick(View v) {
        String phone = edit_phone.getText().toString().trim();

        HttpManager.getInstance().post(this, new JsonHttpHandler<String>(Requests.getValidateCode(phone, "")) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {

            }
        });
    }


}
