package com.smileab.mobile.practice.cell;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * Created by Admin on 2017/12/26.
 */
public class ClassifyCell extends RelativeLayout {

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


    private void initData() {

//        FrescoImageHelper.getImageForSmall();

    }

}
