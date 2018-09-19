package com.ktc.tvhelper.networkdiagnose.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DotView extends View {

    private int mWidth;
    private int mHeight;
    private final int radius = 8;

    private Paint mPaint;
    private final int DEFAULT_DOT_COLOR = Color.argb(250, 72, 209, 204);

    public DotView(Context context) {
        super(context);
        initPaint();
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public DotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mWidth = getMeasuredWidth() / 2;
//        mHeight = getMeasuredHeight() / 2;
//        Log.d("fengjw", "mWidth : " + mWidth);
//        Log.d("fengjw", "mHeight : " + mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w / 2;
        mHeight = h / 2;
        Log.d("fengjw", "mWidth : " + mWidth);
        Log.d("fengjw", "mHeight : " + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth, mHeight, radius, mPaint);
    }

    private void initPaint(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(DEFAULT_DOT_COLOR);
        mPaint.setStyle(Paint.Style.FILL);
    }

}
