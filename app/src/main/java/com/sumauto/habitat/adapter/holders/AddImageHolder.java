package com.sumauto.habitat.adapter.holders;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sumauto.habitat.R;
import com.sumauto.habitat.utils.ImageOptions;

/**
 * Created by Lincoln on 16/3/24.
 * 可删除的图片
 */
public class AddImageHolder extends BaseViewHolder {
    public AddImageHolder(ViewGroup parent) {
        super(parent, R.layout.grid_item_add_able_image);
    }
}
