package com.sumauto.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.view.View.OnClickListener;

public class ViewUtil {


    public static void setBackground(View view, Drawable d) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(d);
        }
    }


    /**
     * 让View变成正方形的
     */
    public static void makeSquare(View v, int size) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp != null) {
            lp.width = size;
            lp.height = size;
        }
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Context context, int resId) {
        int color;
        if (Build.VERSION.SDK_INT >= 23) {
            color = context.getResources().getColor(resId, null);
        } else {
            color = context.getResources().getColor(resId);
        }
        return color;
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int resId) {
        Drawable d;

        if (Build.VERSION.SDK_INT >= 21) {
            d = context.getResources().getDrawable(resId, null);
        } else {
            d = context.getResources().getDrawable(resId);
        }
        return d;
    }

    public static void registerOnClickListener(OnClickListener l, View... views) {
        for (View v : views) {
            v.setOnClickListener(l);
        }
    }

    public static void closeKeyboard(EditText editText){
        InputMethodManager imm= (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public static void openKeyboard(EditText editText){
        InputMethodManager imm= (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,InputMethodManager.RESULT_SHOWN);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}
