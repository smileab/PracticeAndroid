package com.smileab.mobile.practice.common;

import android.app.Application;

/**
 * Created by Admin on 2018/1/3.
 */

public class AppApplication extends Application {


    private static AppApplication mAppApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
    }

    public static AppApplication getInstance() {
        return mAppApplication;
    }

}
