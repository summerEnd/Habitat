package com.sumauto.habitat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.sumauto.habitat.R;
import com.sumauto.habitat.http.HttpHandler;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.widget.SmsButton;

import org.json.JSONException;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    EditText editText;
    private EditText edit_phone;
    private EditText edit_password;
    private EditText edit_sms_code;
    SmsButton smsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_sms_code = (EditText) findViewById(R.id.edit_sms_code);
        smsButton = (SmsButton) findViewById(R.id.btn_sms);
        smsButton.setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sms: {
                String phone = edit_phone.getText().toString().trim();
                RequestParams requestParams = new RequestParams();
                requestParams.put("action", "reg");
                requestParams.put("phone", phone);
                HttpManager.getInstance().postApi(this, HttpManager.getValidateCode, requestParams, new HttpHandler() {
                    @Override
                    public void onSuccess(HttpResponse response) throws JSONException {

                    }
                });
                break;
            }
            case R.id.btn_register: {
                String phone = edit_phone.getText().toString().trim();
                RequestParams requestParams = new RequestParams();
                requestParams.put("code", edit_sms_code.getText().toString().trim());
                requestParams.put("pwd", edit_password.getText().toString().trim());
                requestParams.put("phone", phone);
                HttpManager.getInstance().postApi(this, HttpManager.getRegister, requestParams, new HttpHandler() {
                    @Override
                    public void onSuccess(HttpResponse response) throws JSONException {

                    }
                });
                break;
            }
        }

    }
}