package com.sumauto.habitat.adapter.holders;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ViewUtil;
import com.sumauto.widget.CheckableLinearLayout;

/**
 * Created by Lincoln on 16/3/22.
 * 黑名单
 */
public class BlackListHolder extends BaseViewHolder {
    public final TextView tv_attention,tv_nick,tv_address;
    public final ImageView iv_avatar;
    private UserInfoBean bean;
    Drawable d;
    public final CheckableLinearLayout btn_remove;

    public BlackListHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_black_user);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        tv_attention = (TextView) itemView.findViewById(R.id.tv_attention);
        tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
        tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        btn_remove = (CheckableLinearLayout) itemView.findViewById(R.id.btn_remove);
        d = ViewUtil.getDrawable(itemView.getContext(), R.drawable.btn_attention_bg);

    }

    @Override
    protected void onInit(Object data) {
        this.bean = (UserInfoBean) data;

        tv_nick.setText(bean.nickname);
        tv_address.setText(bean.title);
        ImageLoader.getInstance().displayImage(bean.headimg,iv_avatar, ImageOptions.options());
    }


    @Override
    public void onClick(View v) {

    }


}
