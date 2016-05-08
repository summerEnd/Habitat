package com.sumauto.habitat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.FeedBean;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.callback.ViewId;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;

import java.util.List;

public class UserDetailActivity extends BaseActivity {
    @ViewId TextView tv_signature;
    @ViewId TextView tv_commit_name;
    @ViewId TextView tv_birthday;
    @ViewId TextView tv_gender;
    @ViewId TextView tv_nick;

    public static void start(Context context, String uid) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "habitat://user/data?"
                + "uid=" + uid;
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String uid = data.getQueryParameter("uid");
            HttpRequest<UserInfoBean> request = Requests.getFriendInfo(uid);

            HttpManager.getInstance().post(this, new JsonHttpHandler<UserInfoBean>(request) {
                @Override
                public void onSuccess(HttpResponse response, HttpRequest<UserInfoBean> request, UserInfoBean bean) {
                    tv_signature.setText(bean.signature);
                    tv_commit_name.setText(bean.community);
                    tv_birthday.setText(bean.birthday);
                    tv_gender.setText(bean.sex);
                    tv_nick.setText(bean.nickname);
                }
            });


        }
    }

}
