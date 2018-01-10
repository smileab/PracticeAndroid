package com.smileab.mobile.practice.cell;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smileab.mobile.practice.R;
import com.smileab.mobile.practice.bean.RepairInitDataResp;
import com.smileab.mobile.practice.common.listener.ListCell;
import com.smileab.mobile.practice.common.listener.ListCell2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class ClassifyContentItemCell extends LinearLayout implements ListCell2 {


    @BindView(R.id.tv)
    TextView tv;

    private Activity mActivity;

    public ClassifyContentItemCell(Context context) {
        super(context);
    }

    public ClassifyContentItemCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this, this);

        if (!isInEditMode()) {
            mActivity = (Activity) getContext();
        }

    }

    @Override
    public void setData(Object obj, int position, BaseAdapter adapter) {

        RepairInitDataResp.RepairCategoryBean.ChildBean data = (RepairInitDataResp.RepairCategoryBean.ChildBean) obj;
        tv.setText(data.getName());

    }

}
