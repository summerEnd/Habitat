package com.sumauto.habitat.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sumauto.habitat.BuildConfig;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.util.ToastUtil;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/23 5.6.6 
 */
public class EditPasswordActivity extends BaseActivity {
    @ViewId(R.id.edit_new_password) EditText edit_new_password;
    @ViewId(R.id.edit_password) EditText edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
    }

    public void onChangePasswordClick(View view) {
        String pwd = string(edit_password);
        final String pwd_new = string(edit_new_password);

        HttpRequest<String> request = Requests.getChangePwd(pwd, pwd_new);

        HttpManager.getInstance().post(this, new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                Account account = getApplicationContext().getLoginAccount();
                manager.setPassword(account, pwd_new);

            }
        });
    }
}
