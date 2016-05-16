package com.sumauto.habitat.adapter.holders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.http.HttpManager;
import com.sumauto.habitat.http.HttpRequest;
import com.sumauto.habitat.http.JsonHttpHandler;
import com.sumauto.habitat.http.Requests;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.widget.CheckableLinearLayout;

/**
 * Created by Lincoln on 16/3/22.
 * 用户item
 */
public class UserListHolder extends BaseViewHolder implements View.OnClickListener {
    public final TextView tv_attention, tv_nick, tv_address;
    public final ImageView iv_avatar;
    private UserInfoBean mBean;
    Drawable d;
    private final CheckableLinearLayout btn_attention;

    public UserListHolder(ViewGroup parent) {
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
        this.mBean = (UserInfoBean) data;
        initAttentionButton();
        ViewUtil.registerOnClickListener(this, btn_attention);
        tv_nick.setText(mBean.nickname);
        tv_address.setText(mBean.title);
        ImageLoader.getInstance().displayImage(mBean.headimg, iv_avatar, ImageOptions.options());
    }


    @Override
    public void onClick(View v) {
        HttpRequest<String> request = mBean.isAttention() ?
                Requests.setUnFollow(mBean.id) : Requests.setFollow(mBean.id);

        HttpManager.getInstance().post(v.getContext(), new JsonHttpHandler<String>(request) {
            @Override
            public void onSuccess(HttpResponse response, HttpRequest<String> request, String bean) {
                mBean.setIsAttention(!mBean.isAttention());
                initAttentionButton();
            }
        });

    }

    void initAttentionButton() {
        btn_attention.setChecked(mBean.isAttention());
        if (mBean.isAttention()) {
            tv_attention.setText(R.string.attention_already);
        } else {
            tv_attention.setText(R.string.attention);
        }
    }
}
