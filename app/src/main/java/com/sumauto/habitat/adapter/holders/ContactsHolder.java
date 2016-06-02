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
 * 用户item
 */
public class ContactsHolder extends BaseViewHolder implements View.OnClickListener {
    public final TextView tv_attention, tv_nick, tv_address;
    public final ImageView iv_avatar;
    public UserInfoBean bean;
    Drawable d;
    private final CheckableLinearLayout btn_attention;
    private String status = "";

    public ContactsHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_user);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        tv_attention = (TextView) itemView.findViewById(R.id.tv_attention);
        tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
        tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        btn_attention = (CheckableLinearLayout) itemView.findViewById(R.id.btn_attention);
        d = ViewUtil.getDrawable(itemView.getContext(), R.drawable.btn_attention_bg);

    }

    @Override
    protected void onInit(Object data) {
        this.bean = (UserInfoBean) data;

        tv_nick.setText(bean.nickname);
        if (!TextUtils.isEmpty(bean.title)) {
            tv_address.setText(bean.title);
        } else {
            tv_address.setText(bean.phone);
        }
        ImageLoader.getInstance().displayImage(bean.headimg, iv_avatar, ImageOptions.options());
    }


    public void setStatus(String status) {
        if (status == null) {
            status = "";
        }
        this.status = status;
        switch (status){
            case "1":{
                tv_attention.setText(R.string.attention);
                btn_attention.setChecked(true);
                break;
            }
            case "2":{
                tv_attention.setText(R.string.attention_already);
                btn_attention.setChecked(false);
                break;
            }
            default:{
                tv_attention.setText("邀请");
                btn_attention.setChecked(true);
            }
        }

    }



}
