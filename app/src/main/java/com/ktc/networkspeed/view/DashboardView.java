package com.ktc.networkspeed.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.lang.annotation.Target;
import java.text.DecimalFormat;

/**
 * @author fengjw
 *
 */
public class DashboardView extends BaseDashboardView {


    //外环画笔
    private Paint mPaintOuterArc;
    //内环画笔
    private Paint mPaintInnerArc;
    //进度点画笔
    private Paint mPaintProgressPoint;
    //刻度文字画笔
    protected Paint mPaintCalibrationText;
    //刻度中间的文字画笔
    protected Paint mPaintCalibrationBetweenText;
    //大刻度画笔
    protected Paint mPaintLargeCalibration;
    //大刻度进度画笔
    protected Paint mPaintProgressLargeCalibration;
    //小刻度画笔
    protected Paint mPaintSmallCalibration;
    //小刻度进度画笔
    protected Paint mPaintProgressSmallCalibration;
    //外环区域
    private RectF mRectOuterArc;
    //内环区域
    private RectF mRectInnerArc;
    //进度条的圆点属性
    private float[] mProgressPointPosition;
    private float mProgressPointRadius;
    //圆环画笔颜色
    private int mOuterArcColor;
    private int mProgressArcColor;
    //内外环之间的间距
    private float mArcSpacing;
    //刻度起始位置和结束位置
    private float mCalibrationStart;
    private float mCalibrationEnd;
    //刻度的文本位置
    private float mCalibrationTextStart;

    //默认圆环之间间距
    private static final int DEFAULT_ARC_SPACING = 30;
    //外环的默认属性
    private static final int DEFAULT_OUTER_ARC_WIDTH = 15;
    private static final int DEFAULT_OUTER_ARC_COLOR = Color.argb(0, 220, 220, 220);
    //内环的默认属性
    private static final float DEFAULT_INNER_ARC_WIDTH = 0.2f;
    private static final int DEFAULT_INNER_ARC_COLOR = Color.argb(50, 0, 1, 0);
    //进度环的默认属性
    private static final int DEFAULT_PROGRESS_ARC_COLOR = Color.argb(200, 112, 34, 22);
    //进度点的默认属性
    private static final float DEFAULT_PROGRESS_POINT_RADIUS = 10;
    private static final int DEFAULT_PROGRESS_POINT_COLOR = Color.argb(250, 255, 0, 255);
    // 大刻度画笔默认值
    private final static float DEFAULT_LARGE_CALIBRATION_WIDTH = 2f;
    private final static int DEFAULT_LARGE_CALIBRATION_COLOR = Color.argb(200, 220, 220, 220);
    // 小刻度画笔默认值
    private final static float DEFAULT_SMALL_CALIBRATION_WIDTH = 0.7f;
    private final static int DEFAULT_SMALL_CALIBRATION_COLOR = Color.argb(100, 192, 192, 192);
    // 默认刻度文字画笔参数
    private final static float DEFAULT_CALIBRATION_TEXT_TEXT_SIZE = 16f;
//    private final static int DEFAULT_CALIBRATION_TEXT_TEXT_COLOR = Color.WHITE;
    private final static int DEFAULT_CALIBRATION_TEXT_TEXT_COLOR = Color.argb(255, 230, 230, 250);

    //内心圆是一个颜色辐射渐变的圆
    private Shader mInnerShader;
    private Paint mInnerPaint;

    //"MB/S" 画笔
    private Paint mUnitPaint;

//    //指示器画笔
//    private Paint mPaintIndicator;
//    //指示器的path
//    private Path mIndicatorPath;
//    //指示器的起始位置
//    private float mIndicatorStart;
//    //指示器默认属性
//    private static final int DEFAULT_INDICATOR_COLOR = Color.argb(200, 255, 255, 255);


    public DashboardView(Context context) {
        this(context,null);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        //默认数据
        mArcSpacing = dp2px(DEFAULT_ARC_SPACING);
        mOuterArcColor = DEFAULT_OUTER_ARC_COLOR;
        mProgressArcColor = DEFAULT_PROGRESS_ARC_COLOR;
        mProgressPointRadius = dp2px(DEFAULT_PROGRESS_POINT_RADIUS);

        //外环画笔
        mPaintOuterArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOuterArc.setStrokeWidth(dp2px(DEFAULT_OUTER_ARC_WIDTH));
        mPaintOuterArc.setStyle(Paint.Style.STROKE);

        //内环画笔
        mPaintInnerArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInnerArc.setStrokeWidth(dp2px(DEFAULT_INNER_ARC_WIDTH));
        mPaintInnerArc.setColor(DEFAULT_INNER_ARC_COLOR);
        mPaintInnerArc.setStyle(Paint.Style.STROKE);

        //进度点画笔
        mPaintProgressPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressPoint.setStyle(Paint.Style.FILL);
        mPaintProgressPoint.setColor(DEFAULT_PROGRESS_POINT_COLOR);

        //大刻度画笔
        mPaintLargeCalibration = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLargeCalibration.setStrokeWidth(dp2px(DEFAULT_LARGE_CALIBRATION_WIDTH));
        mPaintLargeCalibration.setColor(DEFAULT_LARGE_CALIBRATION_COLOR);

        //大刻度进度画笔
        mPaintProgressLargeCalibration = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressLargeCalibration.setStrokeWidth(dp2px(DEFAULT_LARGE_CALIBRATION_WIDTH));
        mPaintProgressLargeCalibration.setColor(DEFAULT_PROGRESS_POINT_COLOR);

        //小刻度画笔
        mPaintSmallCalibration = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSmallCalibration.setStrokeWidth(dp2px(DEFAULT_SMALL_CALIBRATION_WIDTH));
        mPaintSmallCalibration.setColor(DEFAULT_SMALL_CALIBRATION_COLOR);

        //小刻度进度画笔
        mPaintProgressSmallCalibration = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressSmallCalibration.setStrokeWidth(dp2px(DEFAULT_SMALL_CALIBRATION_WIDTH));
        mPaintProgressSmallCalibration.setColor(DEFAULT_PROGRESS_POINT_COLOR);

        //刻度文字画笔
        mPaintCalibrationText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCalibrationText.setTextAlign(Paint.Align.CENTER);
        mPaintCalibrationText.setTextSize(sp2px(DEFAULT_CALIBRATION_TEXT_TEXT_SIZE));
        mPaintCalibrationText.setColor(DEFAULT_CALIBRATION_TEXT_TEXT_COLOR);

        //刻度中间的文字画笔
        mPaintCalibrationBetweenText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCalibrationBetweenText.setTextAlign(Paint.Align.CENTER);
        mPaintCalibrationBetweenText.setTextSize(sp2px(DEFAULT_CALIBRATION_TEXT_TEXT_SIZE));
        mPaintCalibrationBetweenText.setColor(DEFAULT_CALIBRATION_TEXT_TEXT_COLOR);

        //进度点的图片
        mProgressPointPosition = new float[2];

        //内部变色圆
        mInnerPaint = new Paint();
        mInnerPaint.setStyle(Paint.Style.FILL);
        mInnerPaint.setAntiAlias(true);

        //"MB/S" 画笔
        mUnitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnitPaint.setTextAlign(Paint.Align.CENTER);
        mUnitPaint.setTextSize(sp2px(25f));
        mUnitPaint.setColor(Color.argb(80, 211, 211, 211));

//        //指示器画笔
//        mPaintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaintIndicator.setStrokeCap(Paint.Cap.SQUARE);
//        mPaintIndicator.setColor(DEFAULT_INDICATOR_COLOR);
//        mPaintIndicator.setStrokeWidth(dp2px(3));
    }

    /**
     * 初始化圆环区域
     */
    @Override
    protected void initArcRect(float left, float top, float right, float bottom) {
        //外环区域
        mRectOuterArc = new RectF(left, top, right, bottom);

        initInnerRect();
    }

    /**
     * 初始化内部的区域
     */
    private void initInnerRect() {
        //内环区域
        mRectInnerArc = new RectF(mRectOuterArc.left + mArcSpacing,mRectOuterArc.top + mArcSpacing,
                mRectOuterArc.right - mArcSpacing , mRectOuterArc.bottom - mArcSpacing);//left 和 top加的原因是都是负数

        //计算刻度位置
        mCalibrationStart = mRectOuterArc.top - mArcSpacing + mPaintOuterArc.getStrokeWidth() / 2; //这个是刻度的阴影线
        mCalibrationEnd = mCalibrationStart + mPaintOuterArc.getStrokeWidth();

        //刻度文字位置
        mCalibrationTextStart = mCalibrationEnd + dp2px(13);

        //指示器路径
//        mIndicatorStart = mRectInnerArc.top + 10;
//        mIndicatorPath = new Path();
//        mIndicatorPath.moveTo(mRadius, mIndicatorStart);
//        mIndicatorPath.rLineTo(-dp2px(6), dp2px(15));
//        mIndicatorPath.rLineTo(dp2px(12), 0);
//        mIndicatorPath.close();
    }

    /**
     * 绘制圆环
     */
    @Override
    protected void drawArc(Canvas canvas, float arcStartAngle, float arcSweepAngle) {
        //绘制外环
//        mPaintOuterArc.setColor(mOuterArcColor);
//        canvas.drawArc(mRectOuterArc, arcStartAngle, arcSweepAngle, false, mPaintOuterArc);
        //绘制内环
//        canvas.translate(mWidth / 2, mHeight / 2);
        Log.d("fengjw1", "mWidth1 = " + mWidth + " mHeight1 = " + mHeight);
        canvas.translate((960 - mWidth) / 2, (540 - mHeight) / 2);
        canvas.drawArc(mRectInnerArc, arcStartAngle, arcSweepAngle, false, mPaintInnerArc);

        //绘制刻度
        drawCalibration(canvas, arcStartAngle);

        //绘制刻度中间的文字
        drawableCalibrationBetweenText(canvas, arcStartAngle);

        int w = (960 - mWidth) / 2;
        int h = (540 - mHeight) / 2;
        int radius = mRadius + 100;
        drawInnerCricle(w + 400, h + 500, radius - 220, canvas, arcStartAngle, arcSweepAngle, false, mPaintInnerArc);

    }

    /**
     * 画内部渐变颜色圆
     */
    private void drawInnerCricle(float dx, float dy,int radius, Canvas canvas, float arcStartAngle, float arcSweepAngle, boolean useCenter, Paint paint){
        mInnerShader = new RadialGradient(dx,dy,radius, Color.parseColor("#1B1B1D"),
                Color.parseColor("#0D243A"),Shader.TileMode.CLAMP);
        mInnerPaint.setShader(mInnerShader);
        canvas.save();
//        canvas.drawCircle(dx, dy, radius, mInnerPaint);
        canvas.drawArc(mRectInnerArc, arcStartAngle, arcSweepAngle, useCenter, mInnerPaint);
        canvas.restore();

    }

    /**
     * 绘制刻度
     */
    private void drawCalibration(Canvas canvas, float arcStartAngle) {
        if(mLargeCalibrationNumber == 0){
            return;
        }
        //旋转画布
        canvas.save();
        canvas.rotate(arcStartAngle - 270, mRadius, mRadius);
        int mod = mLargeBetweenCalibrationNumber + 1;
        //遍历数量
        for (int i = 0; i < mCalibrationTotalNumber; i++) {
            //绘制刻度线
            if(i % mod == 0){
                canvas.drawLine(mRadius, mCalibrationStart, mRadius, mCalibrationEnd, mPaintLargeCalibration);
                //绘制刻度文字
                int index = i / mod;
                if(mCalibrationNumberText != null && mCalibrationNumberText.length > index){
                    canvas.drawText(String.valueOf(mCalibrationNumberText[index]), mRadius, mCalibrationTextStart, mPaintCalibrationText);
                }
            } else {
                canvas.drawLine(mRadius, mCalibrationStart, mRadius, mCalibrationEnd, mPaintSmallCalibration);
            }
            //旋转
            canvas.rotate(mSmallCalibrationBetweenAngle, mRadius, mRadius);
        }
        canvas.restore();
    }

    /**
     * 绘制刻度文本
     */
    private void drawableCalibrationBetweenText(Canvas canvas, float arcStartAngle) {
        //如果没有设置大刻度的文字或者大刻度的文字数量和刻度数量不同
//        if(mCalibrationBetweenText == null || mCalibrationBetweenText.length == 0) {
//            return;
//        }
        //旋转画布
//        canvas.save();
//        canvas.rotate(arcStartAngle - 270 + mLargeCalibrationBetweenAngle / 2, mRadius, mRadius);
//        //需要绘制的数量
//        int number = mLargeCalibrationNumber - 1;
//        //遍历
//        for (int i = 0; i < number; i++) {
//            canvas.drawText(mCalibrationBetweenText[i], mRadius, mCalibrationTextStart, mPaintCalibrationBetweenText);
//            canvas.rotate(mLargeCalibrationBetweenAngle, mRadius, mRadius);
//        }
//        canvas.restore();
    }

    /**
     * 绘制进度圆环
     */
    @Override
    protected void drawProgressArc(Canvas canvas, float arcStartAngle, float progressSweepAngle) {
        if(progressSweepAngle == 0) {
            return;
        }
        Path path = new Path();
        //添加进度圆环的区域
        path.addArc(mRectInnerArc, arcStartAngle, progressSweepAngle);
        //计算切线值和为重
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), mProgressPointPosition, null);
        //绘制圆环
//        mPaintInnerArc.setColor(mProgressArcColor);
//        canvas.drawPath(path, mPaintInnerArc);
        //绘制进度点
        if(mProgressPointPosition[0] != 0 && mProgressPointPosition[1] != 0) {
            canvas.drawCircle(mProgressPointPosition[0], mProgressPointPosition[1], mProgressPointRadius, mPaintProgressPoint);
        }


        drawProgressCalibration(canvas, arcStartAngle, progressSweepAngle);

        //绘制指针
//        canvas.save();
//        canvas.rotate(arcStartAngle + progressSweepAngle - 270, mRadius, mRadius);
//        mPaintIndicator.setStyle(Paint.Style.FILL);
//        canvas.drawPath(mIndicatorPath, mPaintIndicator);
//        mPaintIndicator.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(mRadius, mIndicatorStart + dp2px(6) + 14, dp2px(6), mPaintIndicator);
//        canvas.restore();
    }

    protected void drawProgressCalibration(Canvas canvas, float arcStartAngle, float progressSweepAngle){
        if(mLargeCalibrationNumber == 0){
            return;
        }
        //旋转画布
        canvas.save();
        canvas.rotate(arcStartAngle - 270, mRadius, mRadius);
        int mod = mLargeBetweenCalibrationNumber + 1;
        int tmp = (int)((progressSweepAngle / mSmallCalibrationBetweenAngle) + 0.5f);
        Log.d("fengjw", "tmp : " + tmp);
        //遍历数量
        for (int i = 0; i < tmp + 1; i++) {
            //绘制刻度线
            if(i % mod == 0){
                canvas.drawLine(mRadius, mCalibrationStart, mRadius, mCalibrationEnd, mPaintProgressLargeCalibration);
                //绘制刻度文字
                int index = i / mod;
                if(mCalibrationNumberText != null && mCalibrationNumberText.length > index){
                    canvas.drawText(String.valueOf(mCalibrationNumberText[index]), mRadius, mCalibrationTextStart, mPaintCalibrationText);
                }
            } else {
                canvas.drawLine(mRadius, mCalibrationStart, mRadius, mCalibrationEnd, mPaintProgressSmallCalibration);
            }
            //旋转
            canvas.rotate(mSmallCalibrationBetweenAngle, mRadius, mRadius);
        }
        canvas.restore();
    }

    /**
     * 绘制文本
     */
    @Override
    protected void drawText(Canvas canvas, float value, String valueLevel, String currentTime) {
        //绘制数值
        canvas.save();
        float marginTop = mRadius + mTextSpacing;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (!TextUtils.isEmpty((String.valueOf(value)))){
            canvas.drawText(String.valueOf(decimalFormat.format(value)), mRadius, marginTop, mPaintValue);

            canvas.drawText("Mbps/S", mRadius, marginTop + getPaintHeight(mUnitPaint, "Mbps/S")+ mTextSpacing, mUnitPaint);
        }
        //绘制数值文字信息
        if(!TextUtils.isEmpty(valueLevel)){
            marginTop = marginTop + getPaintHeight(mPaintValueLevel, valueLevel) + mTextSpacing;
            canvas.drawText(valueLevel, mRadius, marginTop, mPaintValueLevel);
        }

        //绘制日期
//        if(!TextUtils.isEmpty(currentTime)) {
//            marginTop = marginTop + getPaintHeight(mPaintDate, currentTime) + mTextSpacing;
//            canvas.drawText(currentTime, mRadius, marginTop, mPaintDate);
//        }
        canvas.restore();
    }

    /**
     * 设置圆环的距离
     */
    public void setArcSpacing(float dpSize){
        mArcSpacing = dp2px(dpSize);

        initInnerRect();

        postInvalidate();
    }

    /**
     * 设置外环颜色
     */
    public void setOuterArcPaint(float dpSize, @ColorInt int color){
        mPaintOuterArc.setStrokeWidth(dp2px(dpSize));
        mOuterArcColor = color;

        postInvalidate();
    }

    /**
     * 设置进度条的颜色
     */
    public void setProgressArcColor(@ColorInt int color){
        mProgressArcColor = color;

        postInvalidate();
    }

    /**
     * 设置内环的属性
     */
    public void setInnerArcPaint(float dpSize, @ColorInt int color){
        mPaintInnerArc.setStrokeWidth(dp2px(dpSize));
        mPaintInnerArc.setColor(color);

        postInvalidate();
    }

    /**
     * 设置进度圆点的属性
     */
    public void setProgressPointPaint(float dpRadiusSize,@ColorInt int color){
        mProgressPointRadius = dp2px(dpRadiusSize);
        mPaintProgressPoint.setColor(color);

        postInvalidate();
    }

    /**
     * 设置大刻度画笔属性
     */
    public void setLargeCalibrationPaint(float dpSize, @ColorInt int color){
        mPaintLargeCalibration.setStrokeWidth(dp2px(dpSize));
        mPaintLargeCalibration.setColor(color);

        postInvalidate();
    }

    /**
     * 设置小刻度画笔属性
     */
    public void setSmallCalibrationPaint(float dpSize, @ColorInt int color){
        mPaintSmallCalibration.setStrokeWidth(dp2px(dpSize));
        mPaintSmallCalibration.setColor(color);

        postInvalidate();
    }

    /**
     * 设置刻度文字画笔属性
     * @param spSize 字体大小
     * @param color 字体颜色
     */
    public void setCalibrationTextPaint(float spSize, @ColorInt int color){
        mPaintCalibrationText.setTextSize(sp2px(spSize));
        mPaintCalibrationText.setColor(color);

        postInvalidate();
    }

    /**
     * 设置刻度中间的文字画笔
     */
    public void setCalibrationBetweenTextPaint(float spSize, @ColorInt int color) {
        mPaintCalibrationBetweenText.setTextSize(sp2px(spSize));
        mPaintCalibrationBetweenText.setColor(color);

        postInvalidate();
    }

}
