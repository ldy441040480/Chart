package com.ldy.drawer.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lidongyang on 2017/10/18.
 */
public class MyProgressBar extends View {

    private static final int MAX = 100;
    private static final float START_ANGLE = 135f;
    private static final float PATH_WIDTH = 80f;

    private int mWidth;
    private int mHeight;
    private int mMax = MAX;
    private int mProgress = 30;
    private Paint mPathPaint;
    private RectF mOval;
    private Paint mArcPaint;
    private Paint mOvalPaint;

    public MyProgressBar(Context context) {
        this(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable();
    }

    private void initVariable() {
        mOval = new RectF();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setDither(true);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setDither(true);

        mOvalPaint = new Paint();
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mOvalPaint.setStyle(Paint.Style.FILL);
        mOvalPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int halfWidth = Math.min(mWidth, mHeight) / 2;
        float sweepAngle = getRateOfProgress() * 360;

        mPathPaint.setStrokeWidth(PATH_WIDTH / 2);
        mPathPaint.setColor(Color.parseColor("#7EE0EA"));
        canvas.drawCircle(halfWidth, halfWidth, halfWidth - PATH_WIDTH / 4, mPathPaint);

        mPathPaint.setStrokeWidth(PATH_WIDTH / 2);
        mPathPaint.setColor(Color.parseColor("#00C1D5"));
        canvas.drawCircle(halfWidth, halfWidth, halfWidth - PATH_WIDTH * 3 / 4, mPathPaint);

        mArcPaint.setStrokeWidth(PATH_WIDTH / 2);
        mArcPaint.setColor(Color.parseColor("#E64666db"));
        mOval.set(
                PATH_WIDTH * 3 / 4,
                PATH_WIDTH * 3 / 4,
                mWidth - PATH_WIDTH * 3 / 4,
                mHeight - PATH_WIDTH * 3 / 4);
        canvas.drawArc(mOval, START_ANGLE, sweepAngle, false, mArcPaint);

        mArcPaint.setStrokeWidth(PATH_WIDTH / 2);
        mArcPaint.setColor(Color.parseColor("#99728af4"));
        mOval.set(
                PATH_WIDTH / 4,
                PATH_WIDTH / 4,
                mWidth - PATH_WIDTH / 4,
                mHeight - PATH_WIDTH / 4);
        canvas.drawArc(mOval, START_ANGLE, sweepAngle, false, mArcPaint);
    }

    private float getRateOfProgress() {
        return (float) mProgress / mMax;
    }

}
