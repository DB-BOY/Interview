package com.gin.interview.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wang.lichen on 2017/7/12.
 */

public class CustomView extends View implements View.OnClickListener, View.OnTouchListener {

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 使用布局文件XML创建CustomView时，在xml文件加载完成后调用这个方法 
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i("tag", "----  onFinishInflate() ----");
    }

    /**
     * CustomView的大小发生改变时调用这个方法 
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("tag", "----  onSizeChanged = " + " w = " + w + "  h = " + h + "  oldW = " + oldw + "  oldH = " + oldw);
    }

    /**
     * 在画布上面绘制 
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.i("tag", "----  dispatchDraw = ");
    }

    /**
     * 绘制CustomView时调用 
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("tag", "----  onDraw ----");
    }

    /**
     * 测量CustomView的大小 
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("tag", "----  onMeasure ----");
    }

    /**
     * 将CustomView放置到父容器中去 
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("tag", "----  onLayout ----");
    }


    /**
     * 将CustomView依附到Window中 
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("tag", "----  onAttachedToWindow ----");
    }

    /**
     * 当手机屏幕从横屏和竖屏 相互转化时调用 
     *
     * @param newConfig
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("tag", "----  onConfigurationChanged ----");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.i("tag", "----  onTouchEvent ----");
        return false;
    }


    @Override
    public void onClick(View v) {

        Log.i("tag", "----  onClick ----");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.i("tag", "----  onTouch ----");
        return true;
    }
}
