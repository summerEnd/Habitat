package com.sumauto.habitat;

import com.sumauto.SApplication;

public class HabitatApp extends SApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        setDebug(BuildConfig.DEBUG);
    }
}
