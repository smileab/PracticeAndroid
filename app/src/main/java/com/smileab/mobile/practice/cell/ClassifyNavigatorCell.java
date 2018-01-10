package com.smileab.mobile.practice.cell;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smileab.mobile.practice.R;
import com.smileab.mobile.practice.bean.RepairInitDataResp;
import com.smileab.mobile.practice.common.listener.ListCell;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class ClassifyNavigatorCell extends LinearLayout implements ListCell {


    @BindView(R.id.tv)
    TextView tv;

    private Activity mActivity;

    public ClassifyNavigatorCell(Context context) {
        super(context);
    }

    public ClassifyNavigatorCell(Context context, AttributeSet attrs) {
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
    public void setData(Object obj, int position, RecyclerView.Adapter adapter) {


        RepairInitDataResp.RepairCategoryBean data = (RepairInitDataResp.RepairCategoryBean) obj;

        tv.setText(data.getName());

    }

}
