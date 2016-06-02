package com.sumauto.habitat.adapter.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.UserInfoBean;
import com.sumauto.habitat.utils.ImageOptions;

/**
 * Created by Lincoln on 16/3/25.
 * 选择用户
 */
public class ChooseUserHolder extends BaseViewHolder implements CompoundButton.OnCheckedChangeListener {
    public final CheckBox cb_select;
    public final TextView tv_nick;
    public final ImageView iv_avatar;
    private UserInfoBean bean;
    private Listener l;

    public ChooseUserHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_choose_user);
        cb_select = (CheckBox) itemView.findViewById(R.id.cb_select);
        tv_nick = (TextView) itemView.findViewById(R.id.tv_nick);
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_select.toggle();
            }
        });
    }

    public void init(UserInfoBean bean,Listener listener,boolean checked) {
        this.bean = bean;
        this.l = listener;
        cb_select.setOnCheckedChangeListener(this);
        cb_select.setChecked(checked);
        tv_nick.setText(bean.getNick());
        ImageLoader.getInstance().displayImage(bean.headimg,iv_avatar, ImageOptions.optionsAvatar());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (l!=null){
            l.onUserSelectChanged(bean,isChecked);
        }
    }
    public interface Listener{
        /**
         * 用户选择
         * @param bean 用户信息
         * @param isSelect 是否选中
         */
        void onUserSelectChanged(UserInfoBean bean,boolean isSelect);
    }
}
