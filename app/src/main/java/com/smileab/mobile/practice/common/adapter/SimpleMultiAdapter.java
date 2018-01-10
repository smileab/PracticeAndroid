package com.smileab.mobile.practice.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smileab.mobile.practice.common.listener.ListCell;
import com.smileab.mobile.practice.common.listener.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emmanuel on 2017/10/31.
 */

public class SimpleMultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int INVALID = -1;

    private Context mContext;
    private List<MultiBean> mData;
    private Map<Integer, Integer> mMapping;

    private OnItemClickListener mItemClickListener = null;

    /**
     * @param context
     * @param data    List<MultiBean> MultiBean.type为viewType，MultiBean.data为数据
     * @param mapping Map<Integer, Integer> key为viewType，value为布局文件
     */
    public SimpleMultiAdapter(Context context, List<MultiBean> data, Map<Integer, Integer> mapping) {
        this.mContext = context;
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mData = data;

        if (mapping == null) {
            mapping = new HashMap<>();
        }
        this.mMapping = mapping;
    }

    public List<MultiBean> getData() {
        return mData;
    }

    public void setData(List<MultiBean> data) {
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

    public MultiBean getItem(int position) {
        if (mData == null || position >= mData.size()) {
            return null;
        }
        return mData.get(position);
    }


    @Override
    public int getItemViewType(int position) {
        MultiBean bean = mData.get(position);
        if (bean != null) {
            return bean.type;
        }
        return INVALID;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Integer layoutId = mMapping.get(viewType);
        if (layoutId != null && layoutId > 0) {
            View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            RecyclerView.ViewHolder holder = new ViewHolder(view);
            return holder;
        }
        View view = new View(mContext);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, final int position) {

        ViewHolder holder = (ViewHolder) h;
        holder.itemView.setTag(position);
        if (holder.itemView instanceof ListCell && getItem(position) != null) {
            ((ListCell) holder.itemView).setData(getItem(position).data, position, this);
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


    public static class MultiBean<T> implements Serializable {

        public int type;
        public T data;

        public MultiBean(int type, T data) {
            this.type = type;
            this.data = data;
        }

    }

    public static class ExtraBean implements Serializable {

        public Object data;
        public Object extra;

        public ExtraBean(Object data, Object extra) {
            this.data = data;
            this.extra = extra;
        }

    }


}
