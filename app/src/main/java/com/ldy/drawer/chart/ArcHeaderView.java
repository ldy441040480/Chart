package com.ldy.drawer.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lidongyang on 2017/10/18.
 */
public class ArcHeaderView extends View {

    private static final int DEFAULT_ARC_HEIGHT = 100;
    private static final int DEFAULT_CONTROL_HEIGHT = 0;
    private static final int DEFAULT_ARC_BACKGROUND_COLOR = 0xFFFF3A80;

    private Paint mPaint;
    private PointF mPointStart, mPointEnd, mPointControl;
    private Path mArcPath;

    private int mWidth;
    private int mHeight;
    private int mBackgroundColor;
    private int mArcHeight;
    private int mControlHeight;
    private float mOffset = 1f;

    public ArcHeaderView(Context context) {
        this(context, null);
    }

    public ArcHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ArcHeaderView);
        mArcHeight = (int) array.getDimension(R.styleable.ArcHeaderView_arcHeight, DEFAULT_ARC_HEIGHT);
        mControlHeight = (int) array.getDimension(R.styleable.ArcHeaderView_controlHeight, DEFAULT_CONTROL_HEIGHT);
        mBackgroundColor = array.getColor(R.styleable.ArcHeaderView_backgroundColor, DEFAULT_ARC_BACKGROUND_COLOR);
        array.recycle();

        init();
    }

    private void init() {
        mArcPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(mBackgroundColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPointStart = new PointF();
        mPointEnd = new PointF();
        mPointControl = new PointF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        resetPath();
    }

    public void setControlOffset(float offset) {
        if (offset > 1) {
            offset = 1;
        }
        if (offset < 0) {
            offset = 0;
        }
        if (offset != mOffset) {
            this.mOffset = offset;
            resetPath();
            invalidate();
        }
    }

    private void resetPath() {
        int offHeight = (int) (mControlHeight * mOffset);

        mPointStart.x = 0;
        mPointStart.y = mHeight - mArcHeight + mControlHeight - offHeight;

        mPointEnd.x = mWidth;
        mPointEnd.y = mHeight - mArcHeight + mControlHeight - offHeight;

        mPointControl.x = mWidth / 2;
        mPointControl.y = mHeight - mArcHeight + offHeight;

        mArcPath.reset();
        mArcPath.moveTo(0, 0);
        mArcPath.addRect(0, 0, mWidth, mHeight - mArcHeight + mControlHeight - offHeight, Path.Direction.CCW);
        mArcPath.moveTo(mPointStart.x, mPointStart.y);
        mArcPath.quadTo(mPointControl.x, mPointControl.y, mPointEnd.x, mPointEnd.y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mArcPath, mPaint);
    }

}
