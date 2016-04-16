package com.sumauto.habitat.adapter.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public void init(CommentBean bean){
        tv_nick.setText("Lincoln"+getAdapterPosition());
        tv_time.setText("2016-03-03 09:00");
        tv_content.setText("人生若只如初见，何事秋风悲画扇。 —— 纳兰性性德 只《木兰词·拟古决绝词柬友》");
        ViewUtil.registerOnClickListener(this,itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
