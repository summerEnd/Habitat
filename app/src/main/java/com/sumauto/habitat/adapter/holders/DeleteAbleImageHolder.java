package com.sumauto.habitat.adapter.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sumauto.habitat.R;
import com.sumauto.widget.recycler.adapter.BaseHolder;

/**
 * Created by Lincoln on 16/3/24.
 * 可删除的图片
 */
public class DeleteAbleImageHolder extends BaseViewHolder {
    public final ImageView iv_image,iv_delete;
    public final View cover;
    public DeleteAbleImageHolder(ViewGroup parent) {
        super(parent, R.layout.grid_item_delete_able_image);
        iv_delete= (ImageView) itemView.findViewById(R.id.iv_delete);
        iv_image= (ImageView) itemView.findViewById(R.id.iv_image);
        cover=itemView.findViewById(R.id.v_cover);
    }

    public void init(){

    }
}
