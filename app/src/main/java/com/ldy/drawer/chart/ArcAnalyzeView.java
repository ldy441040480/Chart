package com.ldy.drawer.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by lidongyang on 2017/10/19.
 */
public class ArcAnalyzeView extends View {

    private static final int ARC_MAX_COUNT = 8;
    private static final int ARC_PATH_COLOR = 0XFF00C1D5;
    private static final int ARC_PADDING_LEFT = 43;
    private static final int ARC_PADDING_RIGHT = 43;
    private static final int ARC_PATH_WIDTH = 10;
    private static final int ARC_PATH_PADDING = 4;

    private static final int   LINE_COLOR = 0XFF979797;
    private static final float LINE_WIDTH = 0.5f;

    private static final int POINT_COLOR        = 0XFFFFFFFF;
    private static final int POINT_RADIUS = 2;
    private static final int POINT_PADDING_LEFT = 6;

    private int mWidth;
    private int mHeight;

    private int mSize;
    private int mRightSize;

    private int mArcPathWidth;
    private int mArcPathPadding;
    private int mArcMaxRadius;
    private int mArcPaddingLeft;
    private int mArcPaddingRight;

    private int mPointRadius;
    private int mPointPaddingLeft;

    private int mLineWidth;
    private int mLineMinTopLength;
    private int mLineMinTopRadius;
    private int mLineBottomRadius;
    private int mLineBottomGap;
    private int mLineBottomLength;

    private int mLineBottomRightPadding;
    private int mLineTopRightPadding;

    private ArrayList<AnalyzeInfo> mAnalyzeInfoList;
    private ArrayList<Point> mPointList;
    private ArrayList<RectF> mArcRectFList;

    private Paint mArcPaint;
    private Paint mPointPaint;
    private Paint mLinePaint;
    private Path mLinePath;
    private RectF mLineRectF;

    public ArcAnalyzeView(Context context) {
        this(context, null);
    }

    public ArcAnalyzeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcAnalyzeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWidth = context.getResources().getDisplayMetrics().widthPixels;

        mArcPathWidth = dip2px(ARC_PATH_WIDTH);
        mArcPaddingLeft = dip2px(ARC_PADDING_LEFT);
        mArcPaddingRight = dip2px(ARC_PADDING_RIGHT);
        mArcPathPadding = dip2px(ARC_PATH_PADDING);

        mPointRadius = dip2px(POINT_RADIUS);
        mPointPaddingLeft = dip2px(POINT_PADDING_LEFT);

        mLineWidth = dip2px(LINE_WIDTH);
        mLineMinTopLength = dip2px(10);
        mLineMinTopRadius = dip2px(4);
        mLineBottomRadius = dip2px(10);
        mLineBottomGap = dip2px(40);
        mLineBottomLength = dip2px(80);
        mLineBottomRightPadding = dip2px(20);
        mLineTopRightPadding = dip2px(30);

        initVariable();
    }

    private void initVariable() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(mArcPathWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setDither(true);
        mArcPaint.setColor(ARC_PATH_COLOR);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setDither(true);
        mPointPaint.setColor(POINT_COLOR);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(LINE_COLOR);
        mLinePaint.setStrokeWidth(mLineWidth);

        mLinePath = new Path();
        mLineRectF = new RectF();

        mArcRectFList = new ArrayList<>();
        mPointList = new ArrayList<>();
    }

    public void setAnalyzeList(ArrayList<AnalyzeInfo> list) {
        if (list != null && !list.isEmpty()) {
            this.mAnalyzeInfoList = list;
            resetValidate();
        }
    }

    private void resetValidate() {
        mArcRectFList.clear();
        mPointList.clear();

        mSize = Math.min(ARC_MAX_COUNT, mAnalyzeInfoList.size());
        int diffOffset = (int) ((mArcPathWidth + mArcPathPadding) * ARC_MAX_COUNT / (float) mSize);
        mArcMaxRadius = (mWidth - mArcPaddingLeft - mArcPaddingRight - diffOffset) / 2;

        int arcCenterX = mWidth / 2;
        int arcCenterY = mArcMaxRadius + mArcPathWidth / 2;

        mRightSize = (int) Math.ceil(mSize / 2f);

        for (int i = 0; i < mSize; i++) {
            int offset = i * (mArcPathWidth + mArcPathPadding);

            RectF rectF = new RectF();
            int left = arcCenterX - mArcMaxRadius + offset;
            int top = arcCenterY - mArcMaxRadius + offset;
            int right = arcCenterX + mArcMaxRadius - offset;
            int bottom = arcCenterY + mArcMaxRadius - offset;
            rectF.set(left, top, right, bottom);
            mArcRectFList.add(rectF);

            Point point = new Point();
            point.x = arcCenterX + mPointPaddingLeft;
            point.y = bottom;
            mPointList.add(point);
        }
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = mArcMaxRadius * 2 + (mRightSize - 1) * mLineBottomGap + mLineTopRightPadding + mLineBottomRightPadding;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnalyzeInfoList == null
                || mAnalyzeInfoList.isEmpty()
                || mArcRectFList.isEmpty()
                || mPointList.isEmpty()) {
            return;
        }
        for (int i = 0; i < mSize; i++) {
            AnalyzeInfo analyzeInfo = mAnalyzeInfoList.get(i);
            float rate = analyzeInfo.rate;
            float sweepAngle = getSweepAngle(rate);

            RectF rectF = mArcRectFList.get(i);
            canvas.drawArc(rectF, 90 - sweepAngle, sweepAngle, false, mArcPaint);

            Point point = mPointList.get(i);

            if (i < mRightSize) {
                drawLinePath(canvas, point, i, Orientation.RIGHT);
            } else {
                drawLinePath(canvas, point, i - mRightSize, Orientation.LEFT);
            }

            canvas.drawCircle(point.x, point.y, mPointRadius, mPointPaint);
        }
    }

    private void drawLinePath(Canvas canvas, Point point, int index, int orientation) {
        mLinePath.reset();
        mLinePath.moveTo(point.x, point.y);
        if (Orientation.RIGHT == orientation) {

            int rightTopRadius = mLineMinTopRadius + mLineMinTopRadius * index;
            int rightTopHorizontalX = point.x + mLineMinTopLength + index * mArcPathWidth;
            int rightTopHorizontalY = point.y;

            int rightVerticalX = rightTopHorizontalX + rightTopRadius;
            int rightVerticalTopY = point.y + rightTopRadius;
            int rightVerticalBottomY = mHeight - mLineBottomRightPadding - (index * mLineBottomGap);

            int rightBottomY = rightVerticalBottomY + mLineBottomRadius;
            int rightBottomLeftX = rightVerticalX + mLineBottomRadius;
            int rightBottomRightX = rightBottomLeftX + mLineBottomLength;

            mLinePath.lineTo(rightTopHorizontalX, point.y);
            mLineRectF.set(
                    rightTopHorizontalX - rightTopRadius,
                    rightTopHorizontalY,
                    rightTopHorizontalX + rightTopRadius,
                    rightTopHorizontalY + 2 * rightTopRadius);
            mLinePath.addArc(mLineRectF, 270, 90);
            mLinePath.moveTo(rightVerticalX, rightVerticalTopY);
            mLinePath.lineTo(rightVerticalX, rightVerticalBottomY);
            mLineRectF.set(
                    rightVerticalX,
                    rightVerticalBottomY - mLineBottomRadius,
                    rightVerticalX + 2 * mLineBottomRadius,
                    rightVerticalBottomY + mLineBottomRadius);
            mLinePath.addArc(mLineRectF, 90, 90);
            mLinePath.moveTo(rightBottomLeftX, rightBottomY);
            mLinePath.lineTo(rightBottomRightX, rightBottomY);
        } else if (Orientation.LEFT == orientation) {

            int leftTopRadius = mLineMinTopRadius + mLineMinTopRadius * index;
            int leftTopHorizontalX = point.x - mLineMinTopLength - index * mArcPathWidth;
            int leftTopHorizontalY = point.y;

            int leftVerticalX = leftTopHorizontalX - leftTopRadius;
            int leftVerticalTopY = point.y + leftTopRadius;
            int leftVerticalBottomY = mHeight - mLineBottomRightPadding - (index * mLineBottomGap);

            int leftBottomY = leftVerticalBottomY + mLineBottomRadius;
            int leftBottomLeftX = leftVerticalX - mLineBottomRadius;
            int leftBottomRightX = leftBottomLeftX + mLineBottomLength;

            mLinePath.lineTo(leftTopHorizontalX, point.y);
            mLineRectF.set(
                    leftTopHorizontalX - leftTopRadius,
                    leftTopHorizontalY,
                    leftTopHorizontalX + leftTopRadius,
                    leftTopHorizontalY + 2 * leftTopRadius);
            mLinePath.addArc(mLineRectF, 270, 90);
            mLinePath.moveTo(leftVerticalX, leftVerticalTopY);
            mLinePath.lineTo(leftVerticalX, leftVerticalBottomY);
            mLineRectF.set(
                    leftVerticalX,
                    leftVerticalBottomY - mLineBottomRadius,
                    leftVerticalX + 2 * mLineBottomRadius,
                    leftVerticalBottomY + mLineBottomRadius);
            mLinePath.addArc(mLineRectF, 90, 90);
            mLinePath.moveTo(leftBottomLeftX, leftBottomY);
            mLinePath.lineTo(leftBottomRightX, leftBottomY);
        }
        canvas.drawPath(mLinePath, mLinePaint);
    }

    private float getSweepAngle(float progress) {
        return progress * 360f;
    }

    private int dip2px(float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getContext().getResources()
                .getDisplayMetrics());
    }

    private static class Orientation {
        private static final int LEFT = 1;
        private static final int RIGHT = 2;
    }

    public static class AnalyzeInfo {

        public String name;
        public float rate;

        public AnalyzeInfo() {
        }

        public AnalyzeInfo(String name, float rate) {
            this.name = name;
            this.rate = rate;
        }
    }

}
