package com.sumauto.habitat.activity.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.HabitatApp;
import com.sumauto.habitat.R;
import com.sumauto.habitat.activity.CreateCommunity;
import com.sumauto.habitat.activity.EditUserInfoActivity;
import com.sumauto.habitat.activity.SettingActivity;
import com.sumauto.habitat.utils.ImageOptions;


/**
 * A simple {@link Fragment} subclass.
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{

    ImageView iv_avatar;
    TextView tv_nick,tv_signature;

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
        iv_avatar= (ImageView) view.findViewById(R.id.iv_avatar);
        tv_nick= (TextView) view.findViewById(R.id.tv_nick);
        tv_signature= (TextView) view.findViewById(R.id.tv_signature);
        HabitatApp app=HabitatApp.getInstance();
        tv_nick.setText(app.getUserData(HabitatApp.ACCOUNT_NICK));
        tv_signature.setText(app.getUserData(HabitatApp.ACCOUNT_SIGNATURE));
        ImageLoader.getInstance().displayImage(app.getUserData(HabitatApp.ACCOUNT_AVATAR),iv_avatar, ImageOptions.options());
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.vg_complete_data:{
                startActivity(new Intent(getActivity(),EditUserInfoActivity.class));
                break;
            }
            case R.id.vg_create_circle:{
                startActivity(new Intent(getActivity(), CreateCommunity.class));
                break;
            }
            case R.id.vg_my_collect:{
                break;
            }
            case R.id.vg_share_place:{
                break;
            }
            case R.id.vg_setting:{
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            }
        }
    }
}
