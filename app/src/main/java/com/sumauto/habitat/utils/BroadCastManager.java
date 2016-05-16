package com.sumauto.habitat.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.sumauto.habitat.HabitatApp;

/*
 * Copyright:	炫彩互动网络科技有限公司
 * Author: 		朱超
 * Description:	处理app所有的广播
 * History:		2016/05/15 5.6.6 
 */
public class BroadCastManager {

    /**
     * 登录状态发生改变
     */
    public static final String ACTION_LOGIN_STATE = "login.state.changed";
    public static final String EXTRA_IS_CHANGED = "login.state.is_changed";

    public static void registerLoginState(Context context, LoginStateReceiver receiver) {
        try {
            IntentFilter filter = new IntentFilter(ACTION_LOGIN_STATE);
            context.registerReceiver(receiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unRegisterReceiver(Context context, BroadcastReceiver receiver) {
        try {
            context.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class LoginStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_LOGIN_STATE)) {
                onChanged(HabitatApp.getInstance().isLogin());
            }
        }

        public void onChanged(boolean isLogin) {

        }
    }

}
