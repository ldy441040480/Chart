package com.ldy.drawer.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by lidongyang on 2017/10/18.
 */
public class CubeProgressBar extends View {

    private static final int DEFAULT_Path_Innner_Color = 0xFFFF3A80;
    private static final long ANIM_DURATION = 500L;
    private static final int MAX = 100;
    private static final float START_ANGLE = 135f;
    private static final float PATH_WIDTH = 80f;
    private static final float OVAL_WIDTH = 20f;

    private Paint mPathPaint;
    private Paint mArcPaint;
    private Paint mOvalPaint;
    private Paint mTextPaint;
    private RectF mArcPath;
    private RectF mOvalPath;
    private Rect mTextBound;

    private int mWidth;
    private int mHeight;
    private int mProgress;

    private int mPathInnnerColor;

    public CubeProgressBar(Context context) {
        this(context, null);
    }

    public CubeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable();
    }

    private void initVariable() {

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeWidth(PATH_WIDTH / 2);
        mPathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setDither(true);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(PATH_WIDTH / 2);
        mArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setDither(true);

        mOvalPaint = new Paint();
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mOvalPaint.setStyle(Paint.Style.FILL);
        mOvalPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#00C1D5"));
        mTextPaint.setTextSize(dip2px(18));

        mArcPath = new RectF();
        mOvalPath = new RectF();
        mTextBound = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }
        if (progress != mProgress) {
            if (progress > 0) {
                ProgressAnimation animation = new ProgressAnimation(progress);
                animation.setDuration(ANIM_DURATION);
                startAnimation(animation);
            } else {
                mProgress = progress;
                initVariable();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = mWidth / 2;
        int centerY = mHeight / 2;
        int radius = Math.min(centerX, centerY);

        float progress = getRateOfProgress(mProgress);
        float sweepAngle = progress * 360;

        mPathPaint.setColor(Color.parseColor("#7EE0EA"));
        canvas.drawCircle(centerX, centerY, radius - PATH_WIDTH / 4, mPathPaint);

        mPathPaint.setColor(Color.parseColor("#00C1D5"));
        canvas.drawCircle(centerX, centerY, radius - PATH_WIDTH * 3 / 4, mPathPaint);

        mArcPaint.setColor(Color.parseColor("#4666db"));
        mArcPath.set(
                PATH_WIDTH * 3 / 4,
                PATH_WIDTH * 3 / 4,
                mWidth - PATH_WIDTH * 3 / 4,
                mHeight - PATH_WIDTH * 3 / 4);
        canvas.drawArc(mArcPath, START_ANGLE, sweepAngle, false, mArcPaint);

        mArcPaint.setColor(Color.parseColor("#99728af4"));
        mArcPath.set(
                PATH_WIDTH / 4,
                PATH_WIDTH / 4,
                mWidth - PATH_WIDTH / 4,
                mHeight - PATH_WIDTH / 4);
        canvas.drawArc(mArcPath, START_ANGLE, sweepAngle, false, mArcPaint);

        mOvalPaint.setColor(Color.parseColor("#74efff"));
        canvas.rotate(sweepAngle + START_ANGLE, centerX, centerY);
        mOvalPath.set(
                radius * 2 - PATH_WIDTH,
                radius - OVAL_WIDTH / 2,
                radius * 2,
                radius + OVAL_WIDTH / 2);
        canvas.drawOval(mOvalPath, mOvalPaint);
        canvas.rotate(-sweepAngle - START_ANGLE, centerX, centerY);

        mOvalPaint.setColor(Color.parseColor("#74efff"));
        canvas.rotate(START_ANGLE, centerX, centerY);
        mOvalPath.set(
                radius * 2 - PATH_WIDTH,
                radius - OVAL_WIDTH / 2,
                radius * 2,
                radius + OVAL_WIDTH / 2);
        canvas.drawOval(mOvalPath, mOvalPaint);
        canvas.rotate(- START_ANGLE, centerX, centerY);

        drawText(canvas, centerX, centerY, progress);
    }

    private void drawText(Canvas canvas, int centerX, int centerY, float progress) {
        String value = (int)(progress * 100) + "%";
        mTextPaint.getTextBounds(value, 0, value.length(), mTextBound);
        canvas.drawText(
                value,
                centerX - mTextBound.width() / 2,
                centerY + mTextBound.height() / 2,
                mTextPaint);
    }

    private float getRateOfProgress(int progress) {
        return (float) progress / MAX;
    }

    private int dip2px(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getContext().getResources()
                .getDisplayMetrics());
    }

    private class ProgressAnimation extends Animation {

        private int animProgress;

        private ProgressAnimation(int animProgress) {
            this.animProgress = animProgress;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mProgress = (int) (animProgress * interpolatedTime);
            postInvalidate();
        }
    }

}
