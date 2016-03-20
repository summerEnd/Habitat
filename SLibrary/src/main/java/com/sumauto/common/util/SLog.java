package com.sumauto.common.util;

import android.util.Log;

import com.sumauto.SApplication;

public class SLog {
    private static String TAG = "SLOG";

    public static void debug_format(String pattern, Object... value) {
        debug(String.format(pattern, value));
    }

    public static void debug(Object value) {
        log(TAG, String.valueOf(value));
    }

    public static void log(String tag, String value) {
        if (SApplication.DEBUG) {
            Log.d(tag, String.valueOf(value));
        }
    }

    public static void error(Object value) {
        error(TAG, String.valueOf(value));
    }

    public static void error(String tag, String value) {
        if (SApplication.DEBUG) {
            Log.e(tag, String.valueOf(value));
        }
    }
}
