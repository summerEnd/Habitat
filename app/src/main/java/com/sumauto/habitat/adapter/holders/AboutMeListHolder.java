package com.sumauto.habitat.adapter.holders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumauto.common.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.widget.CheckableLinearLayout;

/**
 * Created by Lincoln on 16/3/22.
 * 用户item
 */
public class AboutMeListHolder extends BaseViewHolder implements View.OnClickListener {
    public final TextView tv_attention;
    private UserInfoBean bean;
    Drawable d;
    private final CheckableLinearLayout btn_attention;

    public AboutMeListHolder(ViewGroup parent) {
        super(parent,R.layout.list_item_about_me_attention);
        tv_attention = (TextView) itemView.findViewById(R.id.tv_attention);
        btn_attention = (CheckableLinearLayout) itemView.findViewById(R.id.btn_attention);
        d = ViewUtil.getDrawable(itemView.getContext(), R.drawable.btn_attention_bg);
    }

    public void init(UserInfoBean bean) {
        this.bean = bean;
        initAttentionButton();
        ViewUtil.registerOnClickListener(this, btn_attention);
    }

    @Override
    public void onClick(View v) {
        bean.setIsAttention(!bean.isAttention());
        initAttentionButton();
    }

    void initAttentionButton() {
        btn_attention.setChecked(bean.isAttention());
        if (bean.isAttention()) {
            tv_attention.setText(R.string.attention_already);
        } else {
            tv_attention.setText(R.string.attention);
        }
    }
}
