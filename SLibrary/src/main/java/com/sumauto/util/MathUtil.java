package com.sumauto.util;

import android.text.TextUtils;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	
 * History:		2016/05/08 5.6.6 
 */
public class MathUtil {
    public static int getInt(String s){
        if (TextUtils.isEmpty(s)){
            return 0;
        }
        try {
            return Integer.decode(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
