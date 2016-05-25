package com.sumauto.habitat.adapter.holders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.bean.AboutBean;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.widget.CheckableLinearLayout;

/**
 * Created by Lincoln on 16/3/22.
 * 用户item
 */
public class AboutMeListHolder extends BaseViewHolder implements View.OnClickListener {
    public final TextView tv_attention,tv_action,tv_nick,tv_time;
    public final ImageView iv_image,iv_avatar;

    Drawable d;
    private final CheckableLinearLayout btn_attention;
    private AboutBean bean;

    public AboutMeListHolder(ViewGroup parent) {
        super(parent,R.layout.list_item_about_me_attention);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_attention = (TextView) itemView.findViewById(R.id.tv_attention);
        tv_action = (TextView) itemView.findViewById(R.id.tv_action);
        tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
        iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);

        btn_attention = (CheckableLinearLayout) itemView.findViewById(R.id.btn_attention);
        d = ViewUtil.getDrawable(itemView.getContext(), R.drawable.btn_attention_bg);
    }

    @Override
    protected void onInit(Object data) {
        super.onInit(data);
        bean = (AboutBean) data;
        tv_nick.setText(bean.nickname);
        tv_action.setText(bean.getActionString());
        tv_time.setText(bean.addtime);

        ImageLoader.getInstance().displayImage(bean.headimg,iv_avatar, ImageOptions.options());
        if (bean.getType()==0){
            //关注
            initAttentionButton();
            btn_attention.setVisibility(View.INVISIBLE);
            iv_image.setVisibility(View.VISIBLE);
            ViewUtil.registerOnClickListener(this, btn_attention);
        }else{
            iv_image.setVisibility(View.INVISIBLE);
            btn_attention.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(bean.timg,iv_image, ImageOptions.options());
        }
    }

    @Override
    public void onClick(View v) {
        bean.setIsFriend(!bean.isFriend());
        initAttentionButton();
    }

    void initAttentionButton() {
        btn_attention.setChecked(bean.isFriend());
        if (bean.isFriend()) {
            tv_attention.setText(R.string.attention_already);
        } else {
            tv_attention.setText(R.string.attention);
        }
    }
}
