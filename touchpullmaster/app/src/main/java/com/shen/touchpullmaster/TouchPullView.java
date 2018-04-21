package com.shen.touchpullmaster;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by pp on 2018/4/19.
 */

public class TouchPullView extends View {

    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mCircleRadius = 50;
    //圆心坐标
    private float mCirclePointX, mCirclePointY;
    //最大可下拉的高度
    private int mDragHeight = 400;
    //下拉进度值
    private float mProgress;
    //目标宽度
    private int mTargetWidth = 400;
    //贝塞尔曲线
    private Path mPath = new Path();
    //贝塞尔画笔
    private Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //中心点最终高度，决定控制点的坐标
    private int mTargetGravityHeight = 10;
    //角度变换0 - 135
    private  int mTargetAngle = 105;
    //一个由快到慢的插值器
    private DecelerateInterpolator mProgressInterpolator = new DecelerateInterpolator();
    private Interpolator mTangentAngleInterpolator;
    private Drawable content = null;
    private int mContentDrawableMargin = 0;


    public TouchPullView(Context context) {
        this(context, null);
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化自定义属性值
     * @param attrs xml对应的属性集合
     */
    private void init(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TouchPullView);
        int color = typedArray.getColor(R.styleable.TouchPullView_pColor, Color.RED);
        mCircleRadius = typedArray.getColor(R.styleable.TouchPullView_pRadius, 50);
        mDragHeight = typedArray.getDimensionPixelOffset(R.styleable.TouchPullView_pDragHeight, 400);
        mTargetAngle = typedArray.getInt(R.styleable.TouchPullView_pTangentAngle, 105);
        mTargetWidth = typedArray.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetWidth, 400);
        mTargetGravityHeight = typedArray.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetGravityHeight, 10);
        content = typedArray.getDrawable(R.styleable.TouchPullView_pContentDrawable);
        mContentDrawableMargin = typedArray.getDimensionPixelOffset(R.styleable.TouchPullView_pContentDrawableMargin, 0);
        typedArray.recycle();

        //初始化画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        mPathPaint = paint;
        //初始化路径插值器
        mTangentAngleInterpolator = PathInterpolatorCompat.create((mCircleRadius * 2) / mDragHeight, 90.0f / mTargetAngle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //基础坐标系改变
        int count = canvas.save();
        //获取平移画布的X的值，随着下滑起始点的坐标移动
        final float transX = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress))/2;
        canvas.translate(transX, 0);
        //绘制贝塞尔
        canvas.drawPath(mPath, mPathPaint);
        //画圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mCirclePaint);
        //绘制Drawable
        if(content != null){
            canvas.save();
            canvas.clipRect(content.getBounds());
            content.draw(canvas);
            canvas.restore();
        }
        canvas.restoreToCount(count);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int MIN_W = (int) (mCircleRadius * 2 + getPaddingLeft() + getPaddingRight());
        int MIN_H = (int) (mDragHeight * mProgress + 0.5f  + getPaddingTop() + getPaddingBottom());

        int widthMeasure = getMeasureSize(widthMeasureSpec, MIN_W);
        int heightMeasure = getMeasureSize(heightMeasureSpec, MIN_H);
        setMeasuredDimension(widthMeasure, heightMeasure);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updatePathLayout();
    }

    /**
     * 获取某一时刻的值
     * @param start         起始点
     * @param end           结束点
     * @param mProgress     当前进度值
     * @return
     */
    private float getValueByLine(float start, float end, float mProgress){
        return start + (end - start) * mProgress;
    }

    /**
     * 获取所需要的宽/高的测量结果
     * @param Spec      测量模式
     * @param minValue  规定的最小值
     * @return  测量结果
     */
    private int getMeasureSize(int Spec, int minValue){
        int result;
        int mode = MeasureSpec.getMode(Spec);
        int size = MeasureSpec.getSize(Spec);
        switch (mode){
            case MeasureSpec.AT_MOST:
                result = Math.min(size, minValue);
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            default:
                result = minValue;
                break;
        }
        return result;
    }

    /**
     * 更新路径
     */
    private void updatePathLayout(){
        final float progress = mProgressInterpolator.getInterpolation(mProgress);

        //获取所有的可绘制的宽/高 此值会根据progress不断的变化
        final float w = getValueByLine(getWidth(), mTargetWidth, mProgress);
        final float h = getValueByLine(0, mDragHeight, mProgress);

        //圆心X坐标
        final float cPointX = w / 2;
        //半径
        final float cRadius = mCircleRadius;
        //圆心Y坐标
        final float cPointY = h - cRadius;
        //控制点结束Y的值
        final float endPointY = mTargetGravityHeight;
        //更新圆心坐标
        mCirclePointX = cPointX;
        mCirclePointY = cPointY;

        final Path path = mPath;
        path.reset();
        path.moveTo(0, 0);
        //坐标系是以最左边的起始点为原点
        //结束点的X,Y坐标
        float lEndPointX, lEndPointY;
        //控制点的坐标
        float lControlPointX, lControlPointY;
        //获取当前切线的弧度
        double angle = mTangentAngleInterpolator.getInterpolation(progress) * mTargetAngle;
        //获取当前弧度
        double radian = Math.toRadians(angle);
        //求出“股”的长度（长的那条直角边）
        float x = (float) (Math.sin(radian) * cRadius);
        //求出“勾”的长度（短的那条直角边）
        float y = (float) (Math.cos(radian) * cRadius);
        //以起始点为原点，x坐标就等于圆的x坐标减去股的长度
        lEndPointX = cPointX - x;
        //以起始点为原点，y坐标就等于圆的y坐标加上勾的长度
        lEndPointY = cPointY + y;
        //获取控制点的y坐标
        lControlPointY = getValueByLine(0, endPointY, progress);
        //结束点和控制点的y坐标差值
        float tHeight = lEndPointY - lControlPointY;
        //通过计算两个角度是相等的，因此弧度依旧适用
        float tWidth = (float) (tHeight/Math.tan(radian));
        //结束点的x-“勾”的长度求出了控制点的x坐标
        lControlPointX = lEndPointX - tWidth;

        //画左边贝塞尔曲线
        path.quadTo(lControlPointX, lControlPointY, lEndPointX, lEndPointY);
        //左右两个结束点相连
        path.lineTo(cPointX + (cPointX - lEndPointX), lEndPointY);
        //画右边贝塞尔曲线
        path.quadTo(cPointX + (cPointX - lControlPointX), lControlPointY, w, 0);
        updateContentLayout(cPointX, cPointY, cRadius);
    }

    private void updateContentLayout(float cx, float cy, float radius){
        Drawable drawable = content;
        if(drawable != null) {
            int margin = mContentDrawableMargin;
            int l = (int) (cx - radius + margin);
            int r = (int) (cx + radius - margin);
            int t = (int) (cy - radius + margin);
            int b = (int) (cy + radius - margin);
            drawable.setBounds(l, t, r, b);
        }
    }

    /**
     * 设置当前进度值
     * @param mProgress
     */
    public void setProgress(float mProgress){
        this.mProgress = mProgress;
        //重新测量
        requestLayout();
    }

    private ValueAnimator valueAnimator;
    public void release(){
        if(valueAnimator == null){
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, 0);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(400);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object val = animation.getAnimatedValue();
                    if(val instanceof Float){
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        }else{
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mProgress, 0);
        }
        valueAnimator.start();
    }

}
