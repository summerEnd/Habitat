package com.sumauto.common.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.OnClickListener;

@SuppressWarnings({"unchecked", "unused"})
public class ViewUtil {

    private View mView;

    public ViewUtil(View view) {
        this.mView = view;
    }

    public <T extends View> T findView(int id) {
        return (T) mView.findViewById(id);
    }

    public <T extends View> T setOnClick(int id, OnClickListener l) {
        T view = (T) mView.findViewById(id);
        view.setOnClickListener(l);
        return view;
    }

    public static <T> T findView(View v, int id) {
        return (T) v.findViewById(id);
    }

    public static void setBackground(View view, Drawable d) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(d);
        }
        else {
            //noinspection deprecation
            view.setBackgroundDrawable(d);
        }
    }

    /**
     * 让View变成正方形的
     */
    public static void makeSquare(View v,int size){
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp!=null){
            lp.width=size;
            lp.height=size;
        }
    }
}
