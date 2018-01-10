package com.smileab.mobile.practice.common.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smileab.mobile.practice.common.listener.ListCell2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 2017/10/31.
 */
public class SimpleListAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> mData;
    private int mLayoutId;

    public SimpleListAdapter(Context context, List<T> data, @LayoutRes int layoutId) {
        this.mContext = context;
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mData = data;
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public T getItem(int position) {
        if (mData == null || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, mLayoutId, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.mView instanceof ListCell2) {
            ((ListCell2) holder.mView).setData(getItem(position), position, this);
        }

        return convertView;
    }


    public static class ViewHolder {

        public View mView;

        public ViewHolder(View view) {
            mView = view;
        }


    }


}
