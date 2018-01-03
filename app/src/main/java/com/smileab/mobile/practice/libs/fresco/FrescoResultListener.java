package com.smileab.mobile.practice.libs.fresco;

public abstract class FrescoResultListener<T> {

    public abstract void onNewResultImpl(T result);

    public void onFailureImpl(Throwable t) {
    }

    public void onProgressUpdate(int progress) {
    }

}