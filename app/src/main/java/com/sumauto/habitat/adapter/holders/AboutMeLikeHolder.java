package com.sumauto.habitat.adapter.holders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;

/**
 * Created by Lincoln on 16/3/22.
 * 用户item
 */
public class AboutMeLikeHolder extends BaseViewHolder implements View.OnClickListener {
    public final TextView tv_attention;
    public final ImageView iv_avatar;
    private UserInfoBean bean;
    Drawable d;

    public AboutMeLikeHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_about_me_like);
        tv_attention = (TextView) itemView.findViewById(R.id.tv_attention);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        d = ViewUtil.getDrawable(itemView.getContext(), R.drawable.btn_attention_bg);

    }

    public void init(UserInfoBean bean) {
        this.bean = bean;
    }

    @Override
    public void onClick(View v) {
    }


}
