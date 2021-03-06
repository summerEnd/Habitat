package com.sumauto.util;

import android.util.Log;

import com.sumauto.SApplication;

public class SLog {
    private static String TAG = "SLOG";



    public static void d(String tag, String pattern, Object... values) {
        String msg;
        if (values != null && values.length > 0) {
            msg = String.format(pattern, values);
        } else {
            msg = pattern;
        }
        if (SApplication.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static int e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }
    public static int e(String tag, String msg) {
        return e(tag, msg,null);
    }
}
