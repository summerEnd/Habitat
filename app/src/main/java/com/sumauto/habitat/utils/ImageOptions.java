package com.sumauto.habitat.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sumauto.habitat.R;

/**
 * Created by Lincoln on 16/3/30.
 */
public class ImageOptions {
    public static DisplayImageOptions options(){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.mipmap.image_loading)
                .showImageOnLoading(R.mipmap.image_loading)
                .showImageOnFail(R.mipmap.image_loading)
                .build();
    }
}
