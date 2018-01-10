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
import com.smileab.mobile.practice.common.adapter.SimpleListAdapter;
import com.smileab.mobile.practice.common.listener.ListCell;
import com.smileab.mobile.practice.common.listener.ListCell2;
import com.smileab.mobile.practice.widget.WrapGridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class ClassifyContentCell extends LinearLayout implements ListCell2 {


    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.grid_view)
    WrapGridView gridView;

    private Activity mActivity;

    public ClassifyContentCell(Context context) {
        super(context);
    }

    public ClassifyContentCell(Context context, AttributeSet attrs) {
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
//    public void setData(Object obj, int position, RecyclerView.Adapter initAdapter) {
    public void setData(Object obj, int position, BaseAdapter initAdapter) {

        RepairInitDataResp.RepairCategoryBean data = (RepairInitDataResp.RepairCategoryBean) obj;

        tv.setText(data.getName());

        SimpleListAdapter adapter = new SimpleListAdapter(mActivity, data.getChild(), R.layout.cell_classify_content_item);
        gridView.setAdapter(adapter);

    }


    @OnClick(R.id.tv)
    public void onViewClicked() {

    }

}
