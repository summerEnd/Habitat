package com.sumauto.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Random;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/15 5.6.6 
 */
public class SUtils {
    /**
     * indicates whether network connectivity exists and it is possible to establish
     * connections and pass data. Always call this before attempting to perform data transactions.
     *
     * @return {@code true} if network connectivity exists, {@code false} otherwise.
     */
    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                // 判断当前网络是否已经连接
                if (info != null && info.isConnected()) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.w("", e);
        }
        return false;
    }

    public static String getUUID(Context context) {
        String MIEI = "miei";

        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String miei = manager.getDeviceId();

        //如果为空就从SharedPreferences中取读
        if (miei == null) {
            miei = context.getSharedPreferences(MIEI, Context.MODE_PRIVATE).getString(MIEI, null);
        } else if (miei.length() == 14) {
            miei += "1";
        }

        //如果还是为空，就随机生成一个，并保存到SharedPreferences
        if (miei == null) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 15; i++) {
                sb.append(random.nextInt(10));
            }
            miei = sb.toString();
            context.getSharedPreferences(MIEI, Context.MODE_PRIVATE).edit().putString(MIEI, miei);
        }

        return miei;
    }
}
