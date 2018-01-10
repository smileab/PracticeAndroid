package com.smileab.mobile.practice.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Admin on 2017/12/18.
 */

public class GsonUtil {

    private static Gson mGson = new Gson();

    private GsonUtil() {
    }

    public static String toJson(Object target) {
        if (target == null) {
            return null;
        }
        try {
            return mGson.toJson(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mGson.fromJson(json, clazz);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeToken<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mGson.fromJson(json, clazz.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
