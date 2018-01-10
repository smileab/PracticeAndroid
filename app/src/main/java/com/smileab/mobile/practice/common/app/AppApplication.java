package com.smileab.mobile.practice.common.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by Admin on 2018/1/3.
 */

public class AppApplication extends Application {


    private static AppApplication mAppApplication;


    public static AppApplication getInstance() {
        return mAppApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;

        init();
    }

    private void  init(){
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

}
