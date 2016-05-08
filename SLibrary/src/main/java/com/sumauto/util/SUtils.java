package com.sumauto.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
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
            context.getSharedPreferences(MIEI, Context.MODE_PRIVATE).edit().putString(MIEI, miei).apply();
        }

        return miei;
    }

    /**
     * 获取字符串的首字母，中文取拼音首字母
     */
    public static String getFirstLetter(String characters) {

        if (TextUtils.isEmpty(characters)) return "";


        char ch = characters.charAt(0);
        if ((ch >> 7) != 0) {
            // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            ch = getFirstLetter(ch);
        }
        return String.valueOf(ch);
    }

    /**
     * 获取拼音首字母缩写
     */
    public static String getShortOfString(String src){
        if (TextUtils.isEmpty(src)) return "";
        int length = src.length();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.valueOf(getFirstLetter(src.charAt(i))));
        }
        return sb.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z'};

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }
}
