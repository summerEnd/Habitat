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
        //微信 appid appsecret
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
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

    public String getPassword() {
        return getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString("password", "");
    }

    public void setPassword(String password) {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString("password", password).apply();
    }

    public String getAccount() {
        return getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString("account", "");
    }

    public void setAccount(String account) {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putString("account", account).apply();
    }
}
