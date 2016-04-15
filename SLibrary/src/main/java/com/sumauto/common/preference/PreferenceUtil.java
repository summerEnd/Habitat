package com.sumauto.common.preference;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public static SharedPreferences getPreference(Context context,Class cls) {
        SharedPreferences sp = context.getSharedPreferences(cls.getSimpleName(), Context.MODE_PRIVATE);

        return sp;
    }
}
