package com.smileab.mobile.practice.common.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/10/30.
 */
public interface ListCell {

    void setData(Object obj, int position, RecyclerView.Adapter adapter);
}
