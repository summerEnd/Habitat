package com.sumauto.habitat.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.adapters.ViewBindingAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/21 5.6.6 
 */
public class ImageBinder extends ViewBindingAdapter{
    public static void setImageUrl(ImageView imageView,String imageUrl){
        ImageLoader.getInstance().displayImage(Constant.TEST_IMAGE_URL,imageView,ImageOptions.options());
    }
}
