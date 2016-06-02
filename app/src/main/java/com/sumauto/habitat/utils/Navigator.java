package com.sumauto.habitat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sumauto.habitat.BuildConfig;
import com.sumauto.habitat.R;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMImage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/31 5.6.6 
 */
public class Navigator {

    public static final String USER_HOST = BuildConfig.USER_HOST;
    public static final String SCHEMA = BuildConfig.SCHEMA;
    public static final String PATH_HOME = "home";//主页
    public static final String PATH_FOLLOW = "follow";//关注
    public static final String PATH_FANS = "fans";//粉丝
    public static final String PATH_TREND = "trend";//动态
    public static final String PATH_DATA = "data";//资料
    public static final String PATH_BLACK = "black";//黑名单

    public static void viewUser(Context context, String path, String uid) {
        //"habitat://user.sumauto.com/fans?uid=?"
        Map<String, String> data = new HashMap<>();
        data.put("uid", uid);
        viewUser(context, path, data);
    }

    /**
     * 查看用户主页
     */
    public static void viewUserHome(Context context, String uid, String avatar) {
        //"habitat://user.sumauto.com/fans?uid=?"
        Map<String, String> data = new HashMap<>();
        data.put("uid", uid);
        data.put("avatar", avatar);
        viewUser(context, PATH_HOME, data);
    }

    public static void viewUser(Context context, String path, Map<String, String> data) {
        //"habitat://user.sumauto.com/fans?uid=?"
        Uri.Builder uri = new Uri.Builder()
                .scheme(SCHEMA)
                .authority(USER_HOST)
                .appendPath(path);
        Set<String> keySet = data.keySet();
        for (String key : keySet) {
            uri.appendQueryParameter(key, data.get(key));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri.build());
        context.startActivity(intent);
    }


}
