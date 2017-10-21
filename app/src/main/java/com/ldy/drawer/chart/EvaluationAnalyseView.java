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
import android.view.View;

import java.util.ArrayList;

/**
 * Created by lidongyang on 2017/10/19.
 */
public class EvaluationAnalyseView extends View {

    private static final int   ARC_MAX_COUNT           = 8;
    private static final int   ARC_PATH_COLOR          = 0XFF00C1D5;
    private static final int   ARC_PADDING_LEFT        = 43;
    private static final int   ARC_PADDING_RIGHT       = 43;
    private static final int   ARC_PATH_WIDTH          = 10;
    private static final int   ARC_PATH_PADDING        = 4;
    private static final float ARC_MIN_RATE            = 0.02f;
    private static final float ARC_DIFF_DISTANCE       = 16f;
    private static final float ARC_DIFF_RATE           = 1.0f;

    private static final int   TEXT_SIZE               = 13;
    private static final int   TEXT_VALUE_MAX_WIDTH    = 72;
    private static final int   TEXT_VALUE_COLOR        = 0XFFAAAAAA;
    private static final int   TEXT_RATE_COLOR         = 0XFF00C1D5;
    private static final int   TEXT_PADDING            = 5;

    private static final float LINE_WIDTH              = 0.5f;
    private static final int   LINE_COLOR              = 0XFF979797;
    private static final int   LINE_TOP_MIN_LENGTH     = 10;
    private static final int   LINE_TOP_MIN_RADIUS     = 4;
    private static final int   LINE_BOTTOM_RADIUS      = 10;
    private static final int   LINE_BOTTOM_GAP         = 40;
    private static final int   LINE_BOTTOM_LENGTH      = 90;
    private static final int   LINE_BOTTOM_PADDING     = 20;
    private static final int   LINE_RIGHT_TOP_PADDING  = 22;
    private static final int   LINE_LEFT_TOP_PADDING   = 32;

    private static final int   POINT_COLOR             = 0XFFFFFFFF;
    private static final int   POINT_RADIUS            = 2;
    private static final int   POINT_PADDING_LEFT      = 6;

    private int mWidth;
    private int mRightSize;
    private int mLeftSize;

    private int mArcPathWidth;
    private int mArcPathPadding;
    private int mArcMaxRadius;
    private int mArcPaddingLeft;
    private int mArcPaddingRight;
    private int mArcDiffDistance;

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
    private int mTextPadding;
    private int mTextValueMaxWidth;

    private ArrayList<EvaluationAnalyseInfo> mAnalyzeInfoList;
    private ArrayList<Point> mPointList;
    private ArrayList<RectF> mArcRectFList;

    private Paint mArcPaint;
    private Paint mPointPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private Path mLinePath;
    private RectF mLineRectF;
    private Rect mTextRateBound;
    private Rect mTextValueBound;

    public EvaluationAnalyseView(Context context) {
        this(context, null);
    }

    public EvaluationAnalyseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EvaluationAnalyseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float density = getResources().getDisplayMetrics().density;
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mArcPathWidth = (int) (ARC_PATH_WIDTH * density);
        mArcPaddingLeft = (int) (ARC_PADDING_LEFT * density);
        mArcPaddingRight = (int) (ARC_PADDING_RIGHT * density);
        mArcPathPadding = (int) (ARC_PATH_PADDING * density);
        mArcDiffDistance = (int) (ARC_DIFF_DISTANCE * density);

        mLinePaintWidth =  (int) (LINE_WIDTH * density);
        mLineTopMinLength = (int) (LINE_TOP_MIN_LENGTH * density);
        mLineTopMinRadius = (int) (LINE_TOP_MIN_RADIUS * density);
        mLineBottomRadius = (int) (LINE_BOTTOM_RADIUS * density);
        mLineBottomGap = (int) (LINE_BOTTOM_GAP * density);
        mLineBottomLength = (int) (LINE_BOTTOM_LENGTH * density);
        mLineBottomPadding = (int) (LINE_BOTTOM_PADDING * density);
        mLineRightTopPadding = (int) (LINE_RIGHT_TOP_PADDING * density);
        mLineLeftTopPadding = (int) (LINE_LEFT_TOP_PADDING * density);

        mPointRadius = (int) (POINT_RADIUS * density);
        mPointPaddingLeft = (int) (POINT_PADDING_LEFT * density);

        mTextSize = (int) (TEXT_SIZE * density);
        mTextPadding = (int) (TEXT_PADDING * density);
        mTextValueMaxWidth = (int) (TEXT_VALUE_MAX_WIDTH * density);

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

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);

        mLinePath = new Path();
        mLineRectF = new RectF();
        mTextRateBound = new Rect();
        mTextValueBound = new Rect();

        mArcRectFList = new ArrayList<>();
        mPointList = new ArrayList<>();
    }

    public void setAnalyzeList(ArrayList<EvaluationAnalyseInfo> list) {
        if (list != null && !list.isEmpty()) {
            int size = Math.min(ARC_MAX_COUNT, list.size());
            int diffWidth = (int) ((ARC_MAX_COUNT - size) * mArcDiffDistance * ARC_DIFF_RATE);
            int maxWidth = mWidth - mArcPaddingLeft - mArcPaddingRight;
            int maxRadius = (maxWidth - diffWidth) / 2;
            int arcCenterX = mWidth / 2;
            int arcCenterY = maxRadius + mArcPathWidth / 2;

            this.mRightSize = (int) Math.ceil(size / 2f);
            this.mLeftSize = size - mRightSize;
            this.mArcMaxRadius = maxRadius;
            this.mAnalyzeInfoList = list;
            this.mArcRectFList.clear();
            this.mPointList.clear();

            for (int i = 0; i < size; i++) {
                int arcPathOffset = i * (mArcPathWidth + mArcPathPadding);
                int left = arcCenterX - mArcMaxRadius + arcPathOffset;
                int top = arcCenterY - mArcMaxRadius + arcPathOffset;
                int right = arcCenterX + mArcMaxRadius - arcPathOffset;
                int bottom = arcCenterY + mArcMaxRadius - arcPathOffset;
                mArcRectFList.add(new RectF(left, top, right, bottom));
                mPointList.add(new Point(arcCenterX + mPointPaddingLeft, bottom));
            }
            requestLayout();
        }
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
        if (isInvalidAnalyse()) {
            return;
        }
        for (int i = 0; i < mAnalyzeInfoList.size(); i++) {
            EvaluationAnalyseInfo analyzeInfo = mAnalyzeInfoList.get(i);
            Point point = mPointList.get(i);
            RectF rectF = mArcRectFList.get(i);

            float rate = analyzeInfo.getRate();
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

    private void drawRightLine(Canvas canvas, Point point, int index, EvaluationAnalyseInfo analyzeInfo) {
        int rightTopRadius = mLineTopMinRadius + mLineTopMinRadius * index;
        int rightTopHorizontalX = point.x + mLineTopMinLength + index * mArcPathWidth;
        int rightTopHorizontalY = point.y;

        int rightVerticalX = rightTopHorizontalX + rightTopRadius;
        int rightVerticalTopY = point.y + rightTopRadius;
        int rightVerticalBottomY = mArcMaxRadius * 2 + mLineRightTopPadding + (mRightSize - index - 1) * mLineBottomGap;

        int rightBottomY = rightVerticalBottomY + mLineBottomRadius;
        int rightBottomLeftX = rightVerticalX + mLineBottomRadius;
        int rightBottomRightX = rightBottomLeftX + mLineBottomLength;

        mLinePath.reset();
        mLinePath.moveTo(point.x, point.y);
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
        drawRightText(canvas, rightBottomLeftX, rightVerticalBottomY, index, analyzeInfo);
    }

    private void drawLeftLine(Canvas canvas, Point point, int index, EvaluationAnalyseInfo analyzeInfo) {
        int leftTopRadius = mLineTopMinRadius + mLineTopMinRadius * index;
        int leftTopHorizontalX = point.x - mLineTopMinLength - index * mArcPathWidth;
        int leftTopHorizontalY = point.y;

        int leftVerticalX = leftTopHorizontalX - leftTopRadius;
        int leftVerticalTopY = point.y + leftTopRadius;
        int leftVerticalBottomY = mArcMaxRadius * 2 + mLineLeftTopPadding + (mLeftSize - index - 1) * mLineBottomGap;

        int leftBottomY = leftVerticalBottomY + mLineBottomRadius;
        int leftBottomRightX = leftVerticalX - mLineBottomRadius;
        int leftBottomLeftX = leftBottomRightX - mLineBottomLength;

        mLinePath.reset();
        mLinePath.moveTo(point.x, point.y);
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
        mLinePath.moveTo(leftBottomRightX, leftBottomY);
        mLinePath.lineTo(leftBottomLeftX, leftBottomY);

        canvas.drawPath(mLinePath, mLinePaint);
        drawLeftText(canvas, leftBottomRightX, leftVerticalBottomY, index, analyzeInfo);
    }

    private void drawRightText(Canvas canvas, float startX, float startY, int index, EvaluationAnalyseInfo analyzeInfo) {
        String value = analyzeInfo.getValue();
        String rate = (int) (analyzeInfo.getRate() * 100) + "%";
        if (value != null && value.length() > 0) {
            float valueWidth = mTextPaint.measureText(value);
            int maxCount = Math.max(ARC_MAX_COUNT / 2, mRightSize);
            int curMaxWidth = mTextValueMaxWidth + (maxCount - index - 1) * mArcPathWidth;
            if (valueWidth > curMaxWidth) {
                int subIndex = mTextPaint.breakText(value, 0, value.length(), true, curMaxWidth, null);
                value = value.substring(0, subIndex - 1) + "...";
            }
            mTextPaint.getTextBounds(value, 0, value.length(), mTextValueBound);
            mTextPaint.setColor(TEXT_VALUE_COLOR);
            canvas.drawText(
                    value,
                    startX,
                    startY,
                    mTextPaint);

            mTextPaint.setColor(TEXT_RATE_COLOR);
            canvas.drawText(
                    rate,
                    startX + mTextValueBound.width() + mTextPadding,
                    startY,
                    mTextPaint);
        }
    }

    private void drawLeftText(Canvas canvas, float startX, float startY, int index, EvaluationAnalyseInfo analyzeInfo) {
        String value = analyzeInfo.getValue();
        String rate = (int) (analyzeInfo.getRate() * 100) + "%";
        if (value != null && value.length() > 0) {
            mTextPaint.getTextBounds(rate, 0, rate.length(), mTextRateBound);
            mTextPaint.setColor(TEXT_RATE_COLOR);
            canvas.drawText(
                    rate,
                    startX - mTextRateBound.width(),
                    startY,
                    mTextPaint);

            float valueWidth = mTextPaint.measureText(value);
            int maxCount = Math.max(ARC_MAX_COUNT / 2, mLeftSize);
            int curMaxWidth = mTextValueMaxWidth + 2 * mPointPaddingLeft + (maxCount - index - 1) * mArcPathWidth;
            if (valueWidth > curMaxWidth) {
                int subIndex = mTextPaint.breakText(value, 0, value.length(), true, curMaxWidth, null);
                value = value.substring(0, subIndex - 1) + "...";
            }
            mTextPaint.getTextBounds(value, 0, value.length(), mTextValueBound);
            mTextPaint.setColor(TEXT_VALUE_COLOR);
            canvas.drawText(
                    value,
                    startX - mTextRateBound.width() - mTextPadding - mTextValueBound.width(),
                    startY,
                    mTextPaint);
        }
    }

    private float getSweepAngle(float rate) {
        return rate < ARC_MIN_RATE ? ARC_MIN_RATE * 360f : rate * 360f;
    }

    private boolean isInvalidAnalyse() {
        return mAnalyzeInfoList == null
                || mAnalyzeInfoList.isEmpty()
                || mArcRectFList.isEmpty()
                || mPointList.isEmpty();
    }

}
