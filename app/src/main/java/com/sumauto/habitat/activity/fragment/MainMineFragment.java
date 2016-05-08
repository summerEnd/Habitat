package com.sumauto.habitat.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.CollectionsActivity;
import com.sumauto.habitat.activity.CreateCommunity;
import com.sumauto.habitat.activity.EditUserInfoActivity;
import com.sumauto.habitat.activity.SettingActivity;
import com.sumauto.habitat.bean.User;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * 我的
 */
public class MainMineFragment extends BaseFragment implements View.OnClickListener {

    ImageView iv_avatar;
    TextView tv_nick, tv_signature, tv_trend, tv_fans, tv_attention;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.vg_my_collect).setOnClickListener(this);
        view.findViewById(R.id.vg_complete_data).setOnClickListener(this);
        view.findViewById(R.id.vg_create_circle).setOnClickListener(this);
        view.findViewById(R.id.vg_share_place).setOnClickListener(this);
        view.findViewById(R.id.vg_setting).setOnClickListener(this);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_nick = (TextView) view.findViewById(R.id.tv_nick);
        tv_trend = (TextView) view.findViewById(R.id.tv_trend);
        tv_fans = (TextView) view.findViewById(R.id.tv_fans);
        tv_attention = (TextView) view.findViewById(R.id.tv_attention);
        tv_signature = (TextView) view.findViewById(R.id.tv_signature);
        initUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserData();
        HttpRequest<User> request = Requests.getUserInfo();
        HttpManager.getInstance().post(getActivity(), new JsonHttpHandler<User>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<User> request, User bean) {
                setUserData(HabitatApp.ACCOUNT_FANS_COUNT,bean.fanscount);
                setUserData(HabitatApp.ACCOUNT_ATTENTION_COUNT,bean.followcount);
                setUserData(HabitatApp.ACCOUNT_TREND_COUNT,bean.articlecount);
                initUserData();
            }
        });
    }

    private void initUserData() {
        if (tv_nick == null) return;

        String nick = getUserData(HabitatApp.ACCOUNT_NICK);
        String signature = getUserData(HabitatApp.ACCOUNT_SIGNATURE);
        String fansCount = getUserData(HabitatApp.ACCOUNT_FANS_COUNT);
        String attentionCount = getUserData(HabitatApp.ACCOUNT_ATTENTION_COUNT);
        String trendCount = getUserData(HabitatApp.ACCOUNT_TREND_COUNT);

        tv_nick.setText(nick);
        tv_signature.setText(signature);
        tv_fans.setText(TextUtils.isEmpty(fansCount)?"0":fansCount);
        tv_attention.setText(TextUtils.isEmpty(attentionCount)?"0":attentionCount);
        tv_trend.setText(TextUtils.isEmpty(trendCount)?"0":trendCount);

        ImageLoader.getInstance().displayImage(getUserData(HabitatApp.ACCOUNT_AVATAR),
                iv_avatar, ImageOptions.options());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.vg_complete_data: {
                startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
                break;
            }
            case R.id.vg_create_circle: {
                startActivity(new Intent(getActivity(), CreateCommunity.class));
                break;
            }
            case R.id.vg_my_collect: {
                CollectionsActivity.start(getActivity(),getUserData(HabitatApp.ACCOUNT_UID));
                break;
            }
            case R.id.vg_share_place: {
                break;
            }
            case R.id.vg_setting: {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            }
        }
    }
}
