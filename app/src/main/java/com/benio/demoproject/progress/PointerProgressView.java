package com.benio.demoproject.progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.benio.demoproject.R;

/**
 * Created by benio on 2016/9/17.
 */
public class PointerProgressView extends View {
    private static final String TAG = "TimeProgressView";

    private int mProgress = 0;
    private int mMaxProgress = 100;

    private float mStartAngle = 135;
    private float mSweepAngle = 270;

    private int mTextSize = 16;
    private int mTextColor = 0xFF000000;
    private Formatter mFormatter = new PercentFormatter();
    private Paint mTextPaint;
    private Rect mTextRect = new Rect();

    // 外环
    private Paint mOuterRingPaint;
    private RectF mOuterRingRectF = new RectF();
    private int mOuterRingDiameter; //外环直径
    private int mOuterRingThickness = 15;
    private int mOuterRingColor = Color.GREEN;

    // 内环
    private Paint mInnerRingPaint;
    private RectF mInnerRingRectF = new RectF();
    //private int mInnerRingDiameter; //内环直径
    private int mInnerRingThickness = 20;
    private int mInnerRingColor = Color.BLUE;

    // 刻度
    private Paint mMarkerPaint;
    private int mMarkerColor = Color.WHITE;
    private int mMarkerLength = 10;
    private int mMarkerWidth = 2;
    private float mMarkerStepSize = 13.5f;

    private int mRingThickness = 30;
    private Bitmap mPointerBitmap;

    public PointerProgressView(Context context) {
        this(context, null, 0);
    }

    public PointerProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointerProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPointerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pointer);
        initPainters();
    }

    private void initPainters() {
        mOuterRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterRingPaint.setStyle(Paint.Style.STROKE);
        mOuterRingPaint.setStrokeWidth(mOuterRingThickness);
        mOuterRingPaint.setColor(mOuterRingColor);

        mInnerRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerRingPaint.setStyle(Paint.Style.STROKE);
        mInnerRingPaint.setStrokeWidth(mInnerRingThickness);
        mInnerRingPaint.setColor(mInnerRingColor);

        mMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkerPaint.setStyle(Paint.Style.STROKE);
        mMarkerPaint.setStrokeWidth(mMarkerWidth);
        mMarkerPaint.setColor(mMarkerColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String text = getText();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);

        int bitmapSize = Math.max(mPointerBitmap.getHeight(), mPointerBitmap.getWidth()) << 1;
        int width = mTextRect.width() + mRingThickness << 1;
        int height = mTextRect.height() + mRingThickness << 1;

        // 获取最大值作为view的初始
        int size = Math.max(bitmapSize, Math.max(width, height));
        int measuredWidth = resolveSizeAndState(size, widthMeasureSpec, 0);
        int measuredHeight = resolveSizeAndState(size, heightMeasureSpec, 0);

        // 获取最小值作为view的确定半径
        // 因为测量出来的width和height可能不相同，所以取最小防止view变形
        int result = Math.min(measuredHeight, measuredWidth);
        setMeasuredDimension(result + getPaddingLeft() + getPaddingRight(),
                result + getPaddingTop() + getPaddingBottom());
        Log.d(TAG, "onMeasure: bitmapSize:" + bitmapSize + ", width:" + width + ", height:" + height + ", size:" + size
                + ", measuredWidth:" + measuredWidth + ", measuredHeight:" + measuredHeight + ", result:" + result);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 去除padding对半径的影响
        w -= getPaddingLeft() + getPaddingRight();
        h -= getPaddingTop() + getPaddingBottom();
        // 重新计算圆周直径
        mOuterRingDiameter = Math.min(w, h);

        // 更新矩形
        mOuterRingRectF.left = mOuterRingThickness;
        mOuterRingRectF.top = mOuterRingThickness;
        mOuterRingRectF.right = mOuterRingDiameter - mOuterRingRectF.left;
        mOuterRingRectF.bottom = mOuterRingDiameter - mOuterRingRectF.top;

        int innerThickness = mRingThickness + mInnerRingThickness;
        mInnerRingRectF.left = mOuterRingRectF.left + innerThickness;
        mInnerRingRectF.top = mOuterRingRectF.top + innerThickness;
        mInnerRingRectF.right = mOuterRingRectF.right - innerThickness;
        mInnerRingRectF.bottom = mOuterRingRectF.bottom - innerThickness;

        Log.d(TAG, "onSizeChanged: " + w + "," + h + ",\n" + mOuterRingRectF + "\n" + mInnerRingRectF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getPaddingLeft(), getPaddingTop());

        final float radius = mOuterRingDiameter / 2f;
        // Draw the text
//        String text = getText();
//        canvas.drawText(text,
//                (mSize - mTextRect.width()) / 2f,
//                // 文字位于圆中心。否则位于半圆之上
//                mTextInCenter ? (mSize + mTextRect.height()) / 2f : mSize / 2f,
//                mTextPaint);


        // Draw the arc
//        float progressSweepAngle = mProgress / (float) mMaxProgress * mSweepAngle;
        canvas.drawArc(mOuterRingRectF, mStartAngle, mSweepAngle, false, mOuterRingPaint);
        canvas.drawArc(mInnerRingRectF, mStartAngle, mSweepAngle, false, mInnerRingPaint);

        // Draw the marker
        final int saveCount = canvas.save();
        canvas.rotate(mStartAngle, radius, radius);
        int count = (int) (mSweepAngle / mMarkerStepSize);
        final float startX = radius - mRingThickness - mInnerRingThickness;
        final float stopX = radius - mMarkerLength;
        for (int i = 0; i < count; i++) {
            canvas.drawLine(0, startX, 0, stopX, mMarkerPaint);
            canvas.rotate(mMarkerStepSize, radius, radius);
        }
        canvas.restoreToCount(saveCount);
    }

    public String getText() {
        return mFormatter != null ? mFormatter.format(mProgress) : "";
    }
}
