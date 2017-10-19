package com.ldy.drawer.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lidongyang on 2017/10/18.
 */
public class TestView extends View {

    private Paint mPaint;
    private Path mPath;
    private RectF mRectF;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.moveTo(100, 100);
        mPath.lineTo(200, 100);
        mRectF.set(150, 100, 250, 200);
        mPath.addArc(mRectF, 270, 90);
        mPath.moveTo(250, 150);
        mPath.lineTo(250, 350);
        mRectF.set(250, 300, 350, 400);
        mPath.addArc(mRectF, 90, 90);
        mPath.moveTo(300, 400);
        mPath.lineTo(550, 400);

        canvas.drawPath(mPath, mPaint);
    }

    private void drawPath() {
        for (int i = 0; i < 4; i ++) {

        }
    }

}
