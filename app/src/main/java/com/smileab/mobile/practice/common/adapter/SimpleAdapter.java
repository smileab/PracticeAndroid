package com.smileab.mobile.practice.common.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smileab.mobile.practice.common.listener.ListCell;
import com.smileab.mobile.practice.common.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmanuel on 2017/10/31.
 */

public class SimpleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> mData;
    private int mLayoutId;

    private OnItemClickListener mItemClickListener = null;

    public SimpleAdapter(Context context, List<T> data, @LayoutRes int layoutId) {
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
    public int getItemCount() {
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {

        ViewHolder holder = (ViewHolder) h;
        holder.itemView.setTag(position);
        if (holder.itemView instanceof ListCell) {
            ((ListCell) holder.itemView).setData(getItem(position), position, this);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(final View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, (int) v.getTag());
                    }
                }
            });

        }
    }


}
