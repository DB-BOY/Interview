package com.gin.interview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by wang.lichen on 2017/7/31.
 */

public class CustomFrame extends FrameLayout implements View.OnTouchListener {
    public CustomFrame(@NonNull Context context) {
        super(context);
    }

    public CustomFrame(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}
