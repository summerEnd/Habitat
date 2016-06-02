package com.sumauto.habitat.adapter.holders;

import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.sumauto.habitat.R;
import com.sumauto.habitat.utils.ImageOptions;

import org.json.JSONObject;

/**
 * Created by Lincoln on 16/3/28.
 * 图片holder
 */
public class ImageViewHolder extends BaseViewHolder {
    private final ImageView iv_image;
    public ImageViewHolder(ViewGroup parent) {
        super(parent, R.layout.grid_item_user_picture);
        iv_image= (ImageView) itemView.findViewById(R.id.iv_image);
    }

    @Override
    protected void onInit(Object data) {
        super.onInit(data);
        JSONObject object= (JSONObject) data;
        //小头像 是s_url
        String url=object.optString("s_url");
        if (TextUtils.isEmpty(url)){
            url=object.optString("url");
        }
        ImageLoader.getInstance().displayImage(
                url,
                new ImageViewAware(iv_image),
                ImageOptions.options());
    }
}
