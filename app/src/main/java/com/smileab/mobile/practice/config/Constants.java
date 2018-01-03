package com.smileab.mobile.practice.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Admin on 2018/1/3.
 */

public class Constants {

    public static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APP_DISK_DIR = SDCARD + File.separator + "Practice" + File.separator;

}
