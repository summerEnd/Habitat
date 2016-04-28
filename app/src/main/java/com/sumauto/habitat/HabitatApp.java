package com.sumauto.habitat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.sumauto.SApplication;
import com.sumauto.habitat.bean.User;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.socialize.PlatformConfig;

import java.lang.reflect.Field;

public class HabitatApp extends SApplication {

    private static HabitatApp instance;
    private static final String PREF_NAME = "configs";

    public static HabitatApp getInstance() {
        return instance;
    }

    User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setDebug(BuildConfig.DEBUG);
        AnalyticsConfig.setAppkey(this, BuildConfig.APP_KEY);
        AnalyticsConfig.setChannel(BuildConfig.CHANNEL);

        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");//微信 appid appsecret
        PlatformConfig.setSinaWeibo("283177913", "76dac8780978535ef7df5e4d62b67847");//新浪微博 appkey appsecret
        PlatformConfig.setQQZone("1105280086", "vFc4waVtlXDxMUVt");// QQ和Qzone appid appkey

    }

    public User geUser() {
        if (mUser == null) {
            mUser = new User();
            SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

            Field[] fields = User.class.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                String name = f.getName();
                try {
                    if (!name.startsWith("$")) //instance run 的 bug
                        f.set(mUser, sp.getString(name, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return mUser;
    }

    @SuppressLint("CommitPrefEdits")
    public void setUser(User user) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        Field[] fields = user.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String name = f.getName();
            try {
                editor.putString(name, (String) f.get(user));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        editor.commit();
        this.mUser = user;
    }
}
