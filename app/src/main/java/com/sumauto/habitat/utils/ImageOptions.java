package com.sumauto.habitat.utils;

import android.graphics.BitmapFactory;

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
    public static DisplayImageOptions options(int w,int h){
        BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
        decodingOptions.outWidth=w;
        decodingOptions.outHeight=h;
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .decodingOptions(decodingOptions)
                .showImageForEmptyUri(R.mipmap.image_loading)
                .showImageOnLoading(R.mipmap.image_loading)
                .showImageOnFail(R.mipmap.image_loading)
                .build();
    }
}
