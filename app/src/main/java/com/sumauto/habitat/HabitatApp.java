package com.sumauto.habitat;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.sumauto.SApplication;
import com.sumauto.habitat.bean.User;

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
                    f.set(mUser,sp.getString(name,""));
                } catch (IllegalAccessException e) {
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
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        editor.commit();
        this.mUser=user;
    }
}
