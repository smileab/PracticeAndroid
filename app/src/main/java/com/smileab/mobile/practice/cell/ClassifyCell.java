package com.smileab.mobile.practice.cell;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.smileab.mobile.practice.R;
import com.smileab.mobile.practice.bean.RepairInitDataResp;
import com.smileab.mobile.practice.common.adapter.SimpleAdapter;
import com.smileab.mobile.practice.common.adapter.SimpleListAdapter;
import com.smileab.mobile.practice.common.listener.OnItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2017/12/26.
 */
public class ClassifyCell extends LinearLayout {


    @BindView(R.id.recyclerview_navigator)
    RecyclerView rvNavigator;
    @BindView(android.R.id.list)
    ListView lvContent;
//    @BindView(R.id.recyclerview_content)
//    RecyclerView rvContent;

    private Activity mActivity;

    public ClassifyCell(Context context) {
        super(context);
    }

    public ClassifyCell(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassifyCell(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            mActivity = (Activity) getContext();
        }

        ButterKnife.bind(this, this);

        initData();

    }

    LinearLayoutManager navVertical;
//    LinearLayoutManager contentVertical;

    private void initData() {

        navVertical = new LinearLayoutManager(mActivity);
        navVertical.setOrientation(LinearLayoutManager.VERTICAL);
        rvNavigator.setLayoutManager(navVertical);

//        contentVertical = new LinearLayoutManager(mActivity);
//        contentVertical.setOrientation(LinearLayoutManager.VERTICAL);
//        rvContent.setLayoutManager(contentVertical);


    }

    public void setData(RepairInitDataResp resp) {

        List<RepairInitDataResp.RepairCategoryBean> repair_category = resp.getRepair_category();
        initNavigator(repair_category);
        initContent(repair_category);

    }

    private void initNavigator(List<RepairInitDataResp.RepairCategoryBean> data) {

        SimpleAdapter<RepairInitDataResp.RepairCategoryBean> mAdapter = new SimpleAdapter(mActivity, data, R.layout.cell_classify_navigator);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                int target = getListViewHeightByPosition(position);
//                Logger.e("=target=" + target + "======getPaddingTop==" + lvContent.getPaddingTop() + "    getTop==" + lvContent.getTop());
//                lvContent.smoothScrollBy(target, 200);

                lvContent.smoothScrollToPosition(position);
//                lvContent.setStackFromBottom(false);
//                lvContent.setSelectionFromTop(position, 0);
//                lvContent.setSelection(position);

//                Logger.e("========onItemClick");
//                rvContent.smoothScrollToPosition(position);
////                rvContent.scrollToPosition();
////                contentVertical.scrollToPosition(position);
//                contentVertical.scrollToPositionWithOffset(position, 0);
//                contentVertical.smoothScrollToPosition(rvContent,RecyclerView.State, position);
//                contentVertical.setStackFromEnd(true);
//                smoothMoveToPosition(rvContent, position);
            }
        });
        rvNavigator.setAdapter(mAdapter);

    }

    private void initContent(List<RepairInitDataResp.RepairCategoryBean> data) {


        SimpleListAdapter mAdapter = new SimpleListAdapter(mActivity, data, R.layout.cell_classify_content);
        lvContent.setAdapter(mAdapter);

//        SimpleAdapter mAdapter = new SimpleAdapter(mActivity, data, R.layout.cell_classify_content);
//        rvContent.setAdapter(mAdapter);

//        rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (mShouldScroll) {
//                    mShouldScroll = false;
//                    smoothMoveToPosition(rvContent, mToPosition);
//                }
//            }
//        });


        lvContent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//                View firstVisibleItemView = lvContent.getChildAt(firstVisibleItem);
//                if (firstVisibleItemView != null) {
//                    Logger.i("v.gettop=" + firstVisibleItemView.getTop() + "    firstVisiblePosition=" + firstVisibleItem);
//                }

            }
        });


    }


    private Map<Integer, Integer> getListViewItemHeight() {
        Map<Integer, Integer> cache = new HashMap<>();
        ListAdapter listAdapter = lvContent.getAdapter();
        if (listAdapter == null) {
            return cache;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            cache.put(i, totalHeight);
            View listItem = listAdapter.getView(i, null, null);
            listItem.measure(0, 0);
            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;
            totalHeight += lvContent.getDividerHeight();
        }
        return cache;
    }

    private int getListViewHeightByPosition(int position) {

        Map<Integer, Integer> cache = getListViewItemHeight();

        int firstVisiblePosition = lvContent.getFirstVisiblePosition();
        View view = lvContent.getChildAt(firstVisiblePosition);

        int target = cache.get(position);
        int current = cache.get(firstVisiblePosition);
//        int offset = view.getTop();

        int result = target - current;

//        Logger.i("view.gettop=" + view.getTop() + "    firstVisiblePosition=" + firstVisiblePosition);
        Logger.i("target=" + target + "    current=" + current + "   result=" + result + "    firstVisiblePosition=" + firstVisiblePosition);
        return result;
    }


//    /**
//     * 目标项是否在最后一个可见项之后
//     */
//    private boolean mShouldScroll;
//    /**
//     * 记录目标项位置
//     */
//    private int mToPosition;
//
//    /**
//     * 滑动到指定位置
//     *
//     * @param mRecyclerView
//     * @param position
//     */
//    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
//        // 第一个可见位置
//        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
//        // 最后一个可见位置
//        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
//
//        if (position < firstItem) {
//            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
//            mRecyclerView.smoothScrollToPosition(position);
//        } else if (position <= lastItem) {
//            // 跳转位置在第一个可见项之后，最后一个可见项之前
//            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
//            int movePosition = position - firstItem;
//            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
//                int top = mRecyclerView.getChildAt(movePosition).getTop();
//                mRecyclerView.smoothScrollBy(0, top);
//            }
//        } else {
//            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
//            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
//            mRecyclerView.smoothScrollToPosition(position);
//            mToPosition = position;
//            mShouldScroll = true;
//        }
//
//    }


}

