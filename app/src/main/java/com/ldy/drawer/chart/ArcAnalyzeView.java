package com.ldy.drawer.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
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

    private static final int TEXT_SIZE = 14;
    private static final int TEXT_NAME_COLOR = 0XFFAAAAAA;

    private static final int LINE_COLOR = 0XFF979797;
    private static final float LINE_WIDTH = 0.5f;

    private static final int POINT_COLOR = 0XFFFFFFFF;
    private static final int POINT_RADIUS = 2;
    private static final int POINT_PADDING_LEFT = 6;

    private int mWidth;
    private int mSize;
    private int mRightSize;
    private int mLeftSize;

    private int mArcPathWidth;
    private int mArcPathPadding;
    private int mArcMaxRadius;
    private int mArcPaddingLeft;
    private int mArcPaddingRight;

    private int mPointRadius;
    private int mPointPaddingLeft;

    private int mLinePaintWidth;
    private int mLineTopMinLength;
    private int mLineTopMinRadius;
    private int mLineBottomRadius;
    private int mLineBottomGap;
    private int mLineBottomLength;
    private int mLineBottomPadding;
    private int mLineLeftTopPadding;
    private int mLineRightTopPadding;

    private int mTextSize;

    private ArrayList<AnalyzeInfo> mAnalyzeInfoList;
    private ArrayList<Point> mPointList;
    private ArrayList<RectF> mArcRectFList;

    private Paint mArcPaint;
    private Paint mPointPaint;
    private Paint mLinePaint;
    private Paint mNamePaint;

    private Path mLinePath;
    private RectF mLineRectF;
    private Rect mNameBound;

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

        mLinePaintWidth = dip2px(LINE_WIDTH);
        mLineTopMinLength = dip2px(10);
        mLineTopMinRadius = dip2px(4);
        mLineBottomRadius = dip2px(10);
        mLineBottomGap = dip2px(40);
        mLineBottomLength = dip2px(90);
        mLineBottomPadding = dip2px(20);
        mLineRightTopPadding = dip2px(25);
        mLineLeftTopPadding = dip2px(35);

        mTextSize = dip2px(14);

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
        mLinePaint.setStrokeWidth(mLinePaintWidth);

        mNamePaint = new Paint();
        mNamePaint.setAntiAlias(true);
        mNamePaint.setTextSize(mTextSize);

        mLinePath = new Path();
        mLineRectF = new RectF();
        mNameBound = new Rect();

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
        mRightSize = (int) Math.ceil(mSize / 2f);
        mLeftSize = mSize - mRightSize;

        int maxWidth = mWidth - mArcPaddingLeft - mArcPaddingRight;
        int diffWidth = (int) ((mArcPathWidth + mArcPathPadding) * ARC_MAX_COUNT / (float) mSize);
        mArcMaxRadius = (maxWidth - diffWidth) / 2;

        int arcCenterX = mWidth / 2;
        int arcCenterY = mArcMaxRadius + mArcPathWidth / 2;

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
        int rightBottomHeight = 0;
        if (mRightSize > 0) {
            rightBottomHeight = (mRightSize - 1) * mLineBottomGap + mLineRightTopPadding;
        }
        int leftBottomHeight = 0;
        if (mLeftSize > 0) {
            leftBottomHeight = (mLeftSize - 1) * mLineBottomGap + mLineLeftTopPadding;
        }
        int height = mArcMaxRadius * 2 + Math.max(leftBottomHeight, rightBottomHeight) + mLineBottomPadding;
        setMeasuredDimension(mWidth, height);
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
            Point point = mPointList.get(i);
            RectF rectF = mArcRectFList.get(i);

            float rate = analyzeInfo.rate;
            float sweepAngle = getSweepAngle(rate);

            canvas.drawArc(rectF, 90 - sweepAngle, sweepAngle, false, mArcPaint);
            if (mRightSize > 0 && i < mRightSize) {
                drawRightLine(canvas, point, i, analyzeInfo);
            } else if (mLeftSize > 0 && i >= mRightSize) {
                drawLeftLine(canvas, point, i - mRightSize, analyzeInfo);
            }
            canvas.drawCircle(point.x, point.y, mPointRadius, mPointPaint);
        }
    }

    private void drawRightLine(Canvas canvas, Point point, int index, AnalyzeInfo analyzeInfo) {
        mLinePath.reset();
        mLinePath.moveTo(point.x, point.y);
        int rightTopRadius = mLineTopMinRadius + mLineTopMinRadius * index;
        int rightTopHorizontalX = point.x + mLineTopMinLength + index * mArcPathWidth;
        int rightTopHorizontalY = point.y;

        int rightVerticalX = rightTopHorizontalX + rightTopRadius;
        int rightVerticalTopY = point.y + rightTopRadius;
        int rightVerticalBottomY = mArcMaxRadius * 2 + mLineRightTopPadding + (mRightSize - index - 1) * mLineBottomGap;

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
        canvas.drawPath(mLinePath, mLinePaint);
        drawRightText(canvas, rightBottomLeftX, rightVerticalBottomY, analyzeInfo);
    }

    private void drawLeftLine(Canvas canvas, Point point, int index, AnalyzeInfo analyzeInfo) {
        mLinePath.reset();
        mLinePath.moveTo(point.x, point.y);
        int leftTopRadius = mLineTopMinRadius + mLineTopMinRadius * index;
        int leftTopHorizontalX = point.x - mLineTopMinLength - index * mArcPathWidth;
        int leftTopHorizontalY = point.y;

        int leftVerticalX = leftTopHorizontalX - leftTopRadius;
        int leftVerticalTopY = point.y + leftTopRadius;
        int leftVerticalBottomY = mArcMaxRadius * 2 + mLineLeftTopPadding + (mLeftSize - index - 1) * mLineBottomGap;

        int leftBottomY = leftVerticalBottomY + mLineBottomRadius;
        int leftBottomLeftX = leftVerticalX - mLineBottomRadius;
        int leftBottomRightX = leftBottomLeftX - mLineBottomLength;

        mLinePath.lineTo(leftTopHorizontalX, point.y);
        mLineRectF.set(
                leftTopHorizontalX - leftTopRadius,
                leftTopHorizontalY,
                leftTopHorizontalX + leftTopRadius,
                leftTopHorizontalY + 2 * leftTopRadius);
        mLinePath.addArc(mLineRectF, 180, 90);
        mLinePath.moveTo(leftVerticalX, leftVerticalTopY);
        mLinePath.lineTo(leftVerticalX, leftVerticalBottomY);
        mLineRectF.set(
                leftVerticalX - 2 * mLineBottomRadius,
                leftVerticalBottomY - mLineBottomRadius,
                leftVerticalX,
                leftVerticalBottomY + mLineBottomRadius);
        mLinePath.addArc(mLineRectF, 0, 90);
        mLinePath.moveTo(leftBottomLeftX, leftBottomY);
        mLinePath.lineTo(leftBottomRightX, leftBottomY);
        canvas.drawPath(mLinePath, mLinePaint);
    }

    private void drawRightText(Canvas canvas, float startX, float startY, AnalyzeInfo analyzeInfo) {

        String value = analyzeInfo.name;
        mNamePaint.setColor(TEXT_NAME_COLOR);
        mNamePaint.getTextBounds(value, 0, value.length(), mNameBound);
        canvas.drawText(
                value,
                startX,
                startY,
                mNamePaint);


    }

    private void drawLeftText(Canvas canvas, Point point, int index, AnalyzeInfo analyzeInfo) {
    }

    private float getSweepAngle(float progress) {
        return progress * 360f;
    }

    private int dip2px(float size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getContext().getResources()
                .getDisplayMetrics());
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
