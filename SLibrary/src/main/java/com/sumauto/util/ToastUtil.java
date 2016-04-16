package com.sumauto.util;

import android.content.Context;
import android.widget.Toast;

import com.sumauto.SApplication;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/04/15 5.6.6 
 */
public class ToastUtil {


    public static void toast(Object obj){
        toast(SApplication.CONTEXT,obj);
    }

    public static void toast(Context context,int resId) {

        try {
            toast(context,context.getString(resId));
        } catch (Exception e) {
            toast(context,Integer.valueOf(resId));
        }
    }

    /**
     * 弹出一个toast,可以在任一线程使用
     *
     * @param o toast的内容
     */
    public static void toast(Context context,Object o) {
        if (null == o)
            return;
        //要toast的信息

        String msg = String.valueOf(o);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast_debug(Object o) {
        if (SApplication.DEBUG) {
            toast(o);
        }
    }
}
