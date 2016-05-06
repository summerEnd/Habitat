package com.sumauto.habitat.adapter.holders;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.utils.ImageOptions;

/**
 * Created by Lincoln on 16/3/23.
 * 头像昵称
 */
public class AvatarNickHolder extends BaseViewHolder{
    TextView tv_nick;
    ImageView iv_avatar;
    public AvatarNickHolder(ViewGroup parent) {
        super(parent, R.layout.grid_item_avatar_nick);
        tv_nick= (TextView) findViewById(R.id.tv_nick);
        iv_avatar= (ImageView) findViewById(R.id.iv_avatar);
    }

    @Override
    protected void onInit(Object data) {
        UserInfoBean bean= (UserInfoBean) data;
        ImageLoader.getInstance().displayImage(bean.headimg,iv_avatar, ImageOptions.options());
        tv_nick.setText(bean.getNick());
    }
}
