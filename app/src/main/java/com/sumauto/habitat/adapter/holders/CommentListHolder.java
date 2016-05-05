package com.sumauto.habitat.adapter.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.utils.ImageOptions;
import com.sumauto.util.ViewUtil;
import com.sumauto.habitat.R;
import com.sumauto.habitat.bean.CommentBean;


public class CommentListHolder extends BaseViewHolder implements View.OnClickListener{
    public final TextView tv_nick,tv_time,tv_content;
    public final ImageView iv_avatar;
    public CommentListHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_comment);
        tv_nick= (TextView) itemView.findViewById(R.id.tv_nick);
        tv_time= (TextView) itemView.findViewById(R.id.tv_time);
        tv_content= (TextView) itemView.findViewById(R.id.tv_content);
        iv_avatar= (ImageView) itemView.findViewById(R.id.iv_avatar);
    }

    @Override
    protected void onInit(Object data) {
        CommentBean bean= (CommentBean) data;
        tv_nick.setText(bean.nickname);
        tv_time.setText(bean.addtime);
        tv_content.setText(bean.content);
        ImageLoader.getInstance().displayImage(bean.headimg,iv_avatar, ImageOptions.options());
        ViewUtil.registerOnClickListener(this,itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
