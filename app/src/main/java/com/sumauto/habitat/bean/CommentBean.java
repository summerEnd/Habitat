package com.sumauto.habitat.bean;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Formatter;

/**
 * Created by Lincoln on 16/3/23.
 */
public class CommentBean {

    /**
     * content : null
     * uid : 3
     * id : 9
     * addtime : 1462284805
     * ruid : 0
     * nickname : luke
     * headimg : http://120.76.138.41/qixidi/upload/headimg/14623797802545.jpg
     * tid : 15
     */

    public String content;
    public String uid;
    public String id;
    public String addtime;
    public String ruid;
    public String nickname;
    public String headimg;
    public String tid;

    public String getAddTime() {
        if (TextUtils.isEmpty(addtime)) return "";
        try {
            Calendar calendar = Calendar.getInstance();
            Long decode = Long.decode(addtime)*1000;
            calendar.setTimeInMillis(decode);
            return DateUtils.getRelativeTimeSpanString(decode).toString();
        } catch (NumberFormatException e) {
            return addtime;
        }

    }
}
