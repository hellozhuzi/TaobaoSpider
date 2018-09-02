package com.zfz.android.taobaospider;


import android.content.Context;

/**
 * Created by lzan13 on 2017/3/9.
 */

public class MyApplication extends android.app.Application {

    private static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
