package com.sumauto.habitat.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Lincoln on 16/3/25.
 * Dialog工具
 */
public class DialogUtils {
    public static void show(Context context,CharSequence text){
        new AlertDialog.Builder(context).setMessage(text).show();
    }
}
