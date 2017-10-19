package com.ldy.drawer.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by lidongyang on 2017/10/18.
 */
public class CubeProgressBar extends View {

    private static final int   PATH_INNER_COLOR   = 0xFF00C1D5;
    private static final int   PATH_OUTER_COLOR   = 0xFF7EE0EA;
    private static final int   ARC_INNER_COLOR    = 0XFF4666DB;
    private static final int   ARC_OUTER_COLOR    = 0xFF6BB1DF;
    private static final int   CUBE_OVAL_COLOR    = 0XFF74EFFF;
    private static final int   CUBE_TEXT_COLOR    = 0XFF00C1D5;

    private static final float CUBE_CIRCLE_ANGLE  = 360f;
    private static final float CUBE_PATH_WIDTH    = 21f;
    private static final float CUBE_TEXT_SIZE     = 16f;
    private static final float CUBE_OVAL_HEIGHT   = 6f;
    private static final float CUBE_START_ANGLE   = 135f;
    private static final float CUBE_MIN_RATE      = 0.05f;
    private static final int   CUBE_MAX           = 100;
    private static final int   CUBE_PROGRESS      = 0;
    private static final long  CUBE_ANIM_DURATION = 500L;

    private Paint mCubePaint;
    private Paint mCubeOvalPaint;
    private Paint mCubeTextPaint;
    private RectF mArcPath;
    private RectF mOvalPath;
    private Rect mCubeTextBound;

    private int mWidth;
    private int mHeight;
    private int mCubeProgress;
    private int mCubeMax;
    private float mCubeMinRate;
    private float mAnimationRate = 1f;

    private int mPathInnerColor;
    private int mPathOuterColor;
    private int mArcInnerColor;
    private int mArcOuterColor;
    private int mCubeOvalColor;
    private int mCubeTextColor;

    private float mCubePathWidth;
    private float mCubeTextSize;
    private float mCubeOvalHeight;
    private float mCubeStartAngle;

    public CubeProgressBar(Context context) {
        this(context, null);
    }

    public CubeProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubeProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CubeProgressBar);
        mPathInnerColor = array.getColor(R.styleable.CubeProgressBar_pathInnerColor, PATH_INNER_COLOR);
        mPathOuterColor = array.getColor(R.styleable.CubeProgressBar_pathOuterColor, PATH_OUTER_COLOR);
        mArcInnerColor = array.getColor(R.styleable.CubeProgressBar_arcInnerColor, ARC_INNER_COLOR);
        mArcOuterColor = array.getColor(R.styleable.CubeProgressBar_arcOuterColor, ARC_OUTER_COLOR);
        mCubeOvalColor = array.getColor(R.styleable.CubeProgressBar_cubeOvalColor, CUBE_OVAL_COLOR);
        mCubeTextColor = array.getColor(R.styleable.CubeProgressBar_cubeTextColor, CUBE_TEXT_COLOR);
        mCubeStartAngle = array.getFloat(R.styleable.CubeProgressBar_cubeStartAngle, CUBE_START_ANGLE);
        mCubePathWidth = array.getDimension(R.styleable.CubeProgressBar_cubePathWidth, dip2px(CUBE_PATH_WIDTH));
        mCubeTextSize = array.getDimension(R.styleable.CubeProgressBar_cubeTextSize, dip2px(CUBE_TEXT_SIZE));
        mCubeOvalHeight = array.getDimension(R.styleable.CubeProgressBar_cubeOvalHeight, dip2px(CUBE_OVAL_HEIGHT));
        mCubeMax = array.getInt(R.styleable.CubeProgressBar_cubeMax, CUBE_MAX);
        mCubeProgress = array.getInt(R.styleable.CubeProgressBar_cubeProgress, CUBE_PROGRESS);
        mCubeMinRate = array.getFloat(R.styleable.CubeProgressBar_cubeMinRate, CUBE_MIN_RATE);
        array.recycle();

        initVariable();
    }

    private void initVariable() {

        mCubePaint = new Paint();
        mCubePaint.setAntiAlias(true);
        mCubePaint.setStrokeWidth(mCubePathWidth / 2);
        mCubePaint.setStyle(Paint.Style.STROKE);
        mCubePaint.setDither(true);

        mCubeOvalPaint = new Paint();
        mCubeOvalPaint.setAntiAlias(true);
        mCubeOvalPaint.setStyle(Paint.Style.FILL);
        mCubeOvalPaint.setDither(true);
        mCubeOvalPaint.setColor(mCubeOvalColor);

        mCubeTextPaint = new Paint();
        mCubeTextPaint.setAntiAlias(true);
        mCubeTextPaint.setColor(mCubeTextColor);
        mCubeTextPaint.setTextSize(mCubeTextSize);

        mArcPath = new RectF();
        mOvalPath = new RectF();
        mCubeTextBound = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public CubeProgressBar setCubeColor(@ColorInt int pathInnerColor, @ColorInt int pathOuterColor,
                             @ColorInt int arcInnerColor, @ColorInt int arcOuterColor,
                             @ColorInt int cubeOvalColor, @ColorInt int cubeTextColor) {
        this.mPathInnerColor = pathInnerColor;
        this.mPathOuterColor = pathOuterColor;
        this.mArcInnerColor = arcInnerColor;
        this.mArcOuterColor = arcOuterColor;
        this.mCubeOvalColor = cubeOvalColor;
        this.mCubeTextColor = cubeTextColor;
        return this;
    }

    public CubeProgressBar setCubePathWidth(float cubePathWidth) {
        this.mCubePathWidth = cubePathWidth;
        return this;
    }

    public CubeProgressBar setCubeTextSize(int cubeTextSize) {
        this.mCubeTextSize = cubeTextSize;
        return this;
    }

    public CubeProgressBar setCubeOvalHeight(int cubeOvalHeight) {
        this.mCubeOvalHeight = cubeOvalHeight;
        return this;
    }

    public CubeProgressBar setCubeStartAngle(int cubeStartAngle) {
        this.mCubeStartAngle = cubeStartAngle;
        return this;
    }

    public CubeProgressBar setCubeMax(int cubeMax) {
        this.mCubeMax = cubeMax;
        return this;
    }

    public CubeProgressBar setCubeMinRate(int cubeMinRate) {
        this.mCubeMinRate = cubeMinRate;
        return this;
    }

    public int getCubeMax() {
        return mCubeMax;
    }

    public void setCubeProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > mCubeMax) {
            progress = mCubeMax;
        }
        if (progress != mCubeProgress) {
            mCubeProgress = progress;
            if (progress > 0) {
                ProgressAnimation animation = new ProgressAnimation();
                animation.setDuration(CUBE_ANIM_DURATION);
                startAnimation(animation);
            } else {
                initVariable();
            }
        }
    }

    public int getCubeProgress() {
        return mCubeProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = mWidth / 2;
        int centerY = mHeight / 2;
        int radius = Math.min(centerX, centerY);

        float realRate = getRateOfProgress(mCubeProgress);
        float sweepAngle = 0f;
        if (realRate > 0) {
            sweepAngle = Math.max(realRate, mCubeMinRate) * CUBE_CIRCLE_ANGLE * mAnimationRate;
        }
        drawCubePath(canvas, centerX, centerY, radius);
        drawCubeArc(canvas, centerX, centerY, radius, sweepAngle);
        drawCubeOval(canvas, centerX, centerY, radius, sweepAngle);
        drawCubeText(canvas, centerX, centerY, realRate * mAnimationRate);
    }

    private float getRateOfProgress(int progress) {
        return (float) progress / mCubeMax;
    }

    private int dip2px(float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getContext().getResources()
                .getDisplayMetrics());
    }

    private void drawCubePath(Canvas canvas, int centerX, int centerY, int radius) {
        mCubePaint.setColor(mPathOuterColor);
        canvas.drawCircle(centerX, centerY, radius - mCubePathWidth / 4, mCubePaint);

        mCubePaint.setColor(mPathInnerColor);
        canvas.drawCircle(centerX, centerY, radius - mCubePathWidth * 3 / 4, mCubePaint);
    }

    private void drawCubeArc(Canvas canvas, int centerX, int centerY, int radius, float sweepAngle) {
        mCubePaint.setColor(mArcInnerColor);
        mArcPath.set(
                centerX - radius + mCubePathWidth * 3 / 4,
                centerY - radius + mCubePathWidth * 3 / 4,
                centerX + radius - mCubePathWidth * 3 / 4,
                centerY + radius - mCubePathWidth * 3 / 4);
        canvas.drawArc(mArcPath, mCubeStartAngle, sweepAngle, false, mCubePaint);

        mCubePaint.setColor(mArcOuterColor);
        mArcPath.set(
                centerX - radius + mCubePathWidth / 4,
                centerY - radius + mCubePathWidth / 4,
                centerX + radius - mCubePathWidth / 4,
                centerY + radius - mCubePathWidth / 4);
        canvas.drawArc(mArcPath, mCubeStartAngle, sweepAngle, false, mCubePaint);
    }

    private void drawCubeOval(Canvas canvas, int centerX, int centerY, int radius, float sweepAngle) {
        canvas.rotate(sweepAngle + mCubeStartAngle, centerX, centerY);
        mOvalPath.set(
                centerX + radius - mCubePathWidth,
                centerY - mCubeOvalHeight / 2,
                centerX + radius,
                centerY + mCubeOvalHeight / 2);
        canvas.drawOval(mOvalPath, mCubeOvalPaint);
        canvas.rotate(-sweepAngle - mCubeStartAngle, centerX, centerY);

        canvas.rotate(mCubeStartAngle, centerX, centerY);
        mOvalPath.set(
                centerX + radius - mCubePathWidth,
                centerY - mCubeOvalHeight / 2,
                centerX + radius,
                centerY + mCubeOvalHeight / 2);
        canvas.drawOval(mOvalPath, mCubeOvalPaint);
        canvas.rotate(-mCubeStartAngle, centerX, centerY);
    }

    private void drawCubeText(Canvas canvas, int centerX, int centerY, float progress) {
        String value = (int) (progress * 100) + "%";
        mCubeTextPaint.getTextBounds(value, 0, value.length(), mCubeTextBound);
        canvas.drawText(
                value,
                centerX - mCubeTextBound.width() / 2,
                centerY + mCubeTextBound.height() / 2,
                mCubeTextPaint);
    }

    private class ProgressAnimation extends Animation {

        private ProgressAnimation() {
            mAnimationRate = 1f;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mAnimationRate = interpolatedTime;
            postInvalidate();
        }
    }

}
