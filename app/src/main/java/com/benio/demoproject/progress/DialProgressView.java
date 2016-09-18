package com.benio.demoproject.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

/**
 * 刻度盘进度条
 * Created by benio on 2016/9/17.
 */
public class DialProgressView extends View {
    private int mProgress = 0;
    private int mMaxProgress = 100;

    private float mStartAngle = 135;
    private float mSweepAngle = 270;

    private int mTextSize = 32;
    private int mTextColor = 0xFFFFFFFF;
    private Formatter mFormatter = new PercentFormatter();
    private Paint mTextPaint;
    private Rect mTextRect = new Rect();

    // 外环
    private Paint mOuterRadiusPaint;
    private RectF mOuterRadiusRectF = new RectF();
    private int mOuterRadiusDiameter; //外环直径，含外环厚度
    private int mOuterRadiusThickness = 2;
    private int mOuterRadiusColor = 0xFFFFFFFF;

    // 内环
    private Paint mInnerRadiusPaint;
    private RectF mInnerRadiusRectF = new RectF();
    private int mInnerRadiusThickness = 8;
    private int mInnerRadiusColor = 0xFFFFFFFF;

    // 刻度
    private Paint mMarkerPaint;
    private int mMarkerColor = 0xFFFFFFFF;
    private int mMarkerLength = 6;
    private int mMarkerWidth = 4;
    private float mMarkerStepSize = 13.5f;

    private Matrix mMatrix;
    private int mThickness = 8;
    private Bitmap mPointerBitmap;
    private Bitmap mIndicatorBitmap;
    private int mPointerOffset = 0;

    public DialProgressView(Context context) {
        this(context, null, 0);
    }

    public DialProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(context, attrs, defStyleAttr);
        initPainters();
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DialProgressView, defStyleAttr, 0);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case R.styleable.DialProgressView_dpv_progress:
                        mProgress = typedArray.getInt(attr, mProgress);
                        break;

                    case R.styleable.DialProgressView_dpv_max:
                        mMaxProgress = typedArray.getInt(attr, mMaxProgress);
                        break;

                    case R.styleable.DialProgressView_dpv_startAngle:
                        mStartAngle = typedArray.getFloat(attr, mStartAngle);
                        break;

                    case R.styleable.DialProgressView_dpv_sweepAngle:
                        mSweepAngle = typedArray.getFloat(attr, mSweepAngle);
                        break;

                    case R.styleable.DialProgressView_dpv_textSize:
                        mTextSize = typedArray.getDimensionPixelSize(attr, mTextSize);
                        break;

                    case R.styleable.DialProgressView_dpv_textColor:
                        mTextColor = typedArray.getColor(attr, mTextColor);
                        break;

                    case R.styleable.DialProgressView_dpv_thickness:
                        mThickness = typedArray.getDimensionPixelSize(attr, mThickness);
                        break;

                    case R.styleable.DialProgressView_dpv_outerRadiusThickness:
                        mOuterRadiusThickness = typedArray.getDimensionPixelSize(attr, mOuterRadiusThickness);
                        break;

                    case R.styleable.DialProgressView_dpv_outerRadiusColor:
                        mOuterRadiusColor = typedArray.getColor(attr, mOuterRadiusColor);
                        break;

                    case R.styleable.DialProgressView_dpv_innerRadiusThickness:
                        mInnerRadiusThickness = typedArray.getDimensionPixelSize(attr, mInnerRadiusThickness);
                        break;

                    case R.styleable.DialProgressView_dpv_innerRadiusColor:
                        mInnerRadiusColor = typedArray.getColor(attr, mInnerRadiusColor);
                        break;

                    case R.styleable.DialProgressView_dpv_markerWidth:
                        mMarkerWidth = typedArray.getDimensionPixelSize(attr, mMarkerWidth);
                        break;

                    case R.styleable.DialProgressView_dpv_markerLength:
                        mMarkerLength = typedArray.getDimensionPixelSize(attr, mMarkerLength);
                        break;

                    case R.styleable.DialProgressView_dpv_markerColor:
                        mMarkerColor = typedArray.getColor(attr, mMarkerColor);
                        break;

                    case R.styleable.DialProgressView_dpv_markerStepSize:
                        mMarkerStepSize = typedArray.getFloat(attr, mMarkerStepSize);
                        break;

                    case R.styleable.DialProgressView_dpv_pointerOffset:
                        mPointerOffset = typedArray.getDimensionPixelSize(attr, mPointerOffset);
                        break;

                    case R.styleable.DialProgressView_dpv_pointer:
                        mPointerBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                        break;

                    case R.styleable.DialProgressView_dpv_indicator:
                        mIndicatorBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(attr, 0));
                        break;
                }
            }
            typedArray.recycle();
        }
    }

    private void initPainters() {
        mOuterRadiusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterRadiusPaint.setStyle(Paint.Style.STROKE);
        mOuterRadiusPaint.setStrokeWidth(mOuterRadiusThickness);
        mOuterRadiusPaint.setColor(mOuterRadiusColor);

        mInnerRadiusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerRadiusPaint.setStyle(Paint.Style.STROKE);
        mInnerRadiusPaint.setStrokeWidth(mInnerRadiusThickness);
        mInnerRadiusPaint.setColor(mInnerRadiusColor);

        mMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkerPaint.setStyle(Paint.Style.STROKE);
        mMarkerPaint.setStrokeWidth(mMarkerWidth);
        mMarkerPaint.setColor(mMarkerColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPointerBitmap != null && !mPointerBitmap.isRecycled()) {
            mPointerBitmap.recycle();
            mPointerBitmap = null;
        }
        if (mIndicatorBitmap != null && !mIndicatorBitmap.isRecycled()) {
            mIndicatorBitmap.recycle();
            mIndicatorBitmap = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String text = getText();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);

        int bitmapSize = 0;
        if (mPointerBitmap != null) {
            bitmapSize = Math.max(mPointerBitmap.getHeight(), mPointerBitmap.getWidth()) << 1;
        }
        if (mIndicatorBitmap != null) {
            int size = Math.max(mIndicatorBitmap.getHeight(), mIndicatorBitmap.getWidth());
            bitmapSize = Math.max(bitmapSize, size);
        }

        int width = mTextRect.width() + mThickness << 1;
        int height = mTextRect.height() + mThickness << 1;

        // 获取最大值作为view的初始
        int size = Math.max(bitmapSize, Math.max(width, height));
        int measuredWidth = resolveSizeAndState(size, widthMeasureSpec, 0);
        int measuredHeight = resolveSizeAndState(size, heightMeasureSpec, 0);

        // 获取最小值作为view的确定半径
        // 因为测量出来的width和height可能不相同，所以取最小防止view变形
        int result = Math.min(measuredHeight, measuredWidth);
        setMeasuredDimension(result + getPaddingLeft() + getPaddingRight(),
                result + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 去除padding对半径的影响
        w -= getPaddingLeft() + getPaddingRight();
        h -= getPaddingTop() + getPaddingBottom();
        // 重新计算圆周直径
        final int size = Math.min(w, h);
        // 更新矩形
        mOuterRadiusRectF.left = mOuterRadiusThickness;
        mOuterRadiusRectF.top = mOuterRadiusThickness;
        mOuterRadiusRectF.right = size - mOuterRadiusRectF.left;
        mOuterRadiusRectF.bottom = size - mOuterRadiusRectF.top;

        int innerThickness = mThickness + mInnerRadiusThickness;
        mInnerRadiusRectF.left = mOuterRadiusRectF.left + innerThickness;
        mInnerRadiusRectF.top = mOuterRadiusRectF.top + innerThickness;
        mInnerRadiusRectF.right = mOuterRadiusRectF.right - innerThickness;
        mInnerRadiusRectF.bottom = mOuterRadiusRectF.bottom - innerThickness;

        mOuterRadiusDiameter = size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getPaddingLeft(), getPaddingTop());

        final float outerRadius = mOuterRadiusDiameter / 2f;

        // Draw the text
        String text = getText();
        canvas.drawText(text,
                (mOuterRadiusDiameter - mTextRect.width()) / 2f,
                outerRadius + outerRadius / 2,
                mTextPaint);

        // Draw the arc
        canvas.drawArc(mOuterRadiusRectF, mStartAngle, mSweepAngle, false, mOuterRadiusPaint);
        canvas.drawArc(mInnerRadiusRectF, mStartAngle, mSweepAngle, false, mInnerRadiusPaint);

        drawPointerAndIndicator(canvas, outerRadius);

        drawMarker(canvas, outerRadius);
    }

    private void drawPointerAndIndicator(Canvas canvas, float outerRadius) {
        // Draw the pointer
        final int saveCount = canvas.save();
        canvas.translate(outerRadius, outerRadius);
        float progressSweepAngle = mProgress / (float) mMaxProgress * mSweepAngle;
        canvas.rotate(progressSweepAngle + mStartAngle);

        if (mMatrix == null) {
            mMatrix = new Matrix();
        }
        if (mPointerBitmap != null) {
            mMatrix.reset();
            // 图片居中显示
            mMatrix.preTranslate(mPointerOffset, -mPointerBitmap.getHeight() / 2);
            canvas.drawBitmap(mPointerBitmap, mMatrix, null);
        }

        // Draw the indicator
        if (mIndicatorBitmap != null) {
            // 计算出内环上的坐标，要求无论内环多'厚'，Indicator图片要在内环上居中
            float dx = outerRadius - mOuterRadiusThickness - mThickness - mInnerRadiusThickness;
            mMatrix.reset();
            mMatrix.preTranslate(dx - mIndicatorBitmap.getHeight() / 2, -mIndicatorBitmap.getHeight() / 2);
            canvas.drawBitmap(mIndicatorBitmap, mMatrix, null);
        }

        canvas.restoreToCount(saveCount);
    }

    private void drawMarker(Canvas canvas, float outerRadius) {
        // Draw the marker
        final int saveCount = canvas.save();
        // 坐标系移到圆的中心，而不是View的中心
        canvas.translate(outerRadius, outerRadius);
        canvas.rotate(mStartAngle);
        // 刻度数量=间隔数+1
        int count = (int) (mSweepAngle / mMarkerStepSize) + 1;
        // 计算出内环的内侧坐标
        // 此处需要先计算stopX。若先计算startX，当mMarkerLength变大时会与圆环重叠
        final float stopX = outerRadius - mOuterRadiusThickness - mThickness - mInnerRadiusThickness - mInnerRadiusThickness / 2;
        final float startX = stopX - mMarkerLength;
        for (int i = 0; i < count; i++) {
            canvas.drawLine(startX, 0, stopX, 0, mMarkerPaint);
            canvas.rotate(mMarkerStepSize);
        }
        canvas.restoreToCount(saveCount);
    }

    public String getText() {
        return mFormatter != null ? mFormatter.format(mProgress) : "";
    }

    public int getPointerOffset() {
        return mPointerOffset;
    }

    public Bitmap getIndicatorBitmap() {
        return mIndicatorBitmap;
    }

    public Bitmap getPointerBitmap() {
        return mPointerBitmap;
    }

    public int getThickness() {
        return mThickness;
    }

    public float getMarkerStepSize() {
        return mMarkerStepSize;
    }

    public int getMarkerWidth() {
        return mMarkerWidth;
    }

    public int getMarkerLength() {
        return mMarkerLength;
    }

    public int getMarkerColor() {
        return mMarkerColor;
    }

    public int getInnerRadiusColor() {
        return mInnerRadiusColor;
    }

    public int getInnerRadiusThickness() {
        return mInnerRadiusThickness;
    }

    public int getOuterRadiusColor() {
        return mOuterRadiusColor;
    }

    public int getOuterRadiusThickness() {
        return mOuterRadiusThickness;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    public float getSweepAngle() {
        return mSweepAngle;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public Formatter getFormatter() {
        return mFormatter;
    }

    public Paint getTextPaint() {
        return mTextPaint;
    }

    public Rect getTextRect() {
        return mTextRect;
    }

    public void setSweepAngle(float sweepAngle) {
        if (sweepAngle > 0 && mSweepAngle != sweepAngle) {
            mSweepAngle = sweepAngle;
            invalidate();
        }
    }

    public void setStartAngle(float startAngle) {
        if (mStartAngle != startAngle) {
            mStartAngle = startAngle;
            invalidate();
        }
    }

    public void setProgress(int progress) {
        if (progress >= 0 && progress <= mMaxProgress && progress != mProgress) {
            mProgress = progress;
            invalidate();
        }
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress > 0 && mMaxProgress != maxProgress) {
            mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setThickness(int thickness) {
        if (thickness > 0 && mThickness != thickness) {
            mThickness = thickness;
            invalidate();
        }
    }

    public void setTextSize(int textSize) {
        if (textSize > 0 && mTextSize != textSize) {
            mTextSize = textSize;
            mTextPaint.setTextSize(textSize);
            invalidate();
        }
    }

    public void setTextColor(int textColor) {
        if (mTextColor != textColor) {
            mTextColor = textColor;
            mTextPaint.setColor(textColor);
            invalidate();
        }
    }

    public void setFormatter(Formatter formatter) {
        if (mFormatter != formatter) {
            mFormatter = formatter;
            invalidate();
        }
    }

    public void setPointerOffset(int pointerOffset) {
        if (mPointerOffset != pointerOffset) {
            mPointerOffset = pointerOffset;
            invalidate();
        }
    }

    public void setIndicator(int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
        setIndicatorBitmap(bitmap);
    }

    public void setIndicatorBitmap(Bitmap indicatorBitmap) {
        if (mIndicatorBitmap != indicatorBitmap) {
            mIndicatorBitmap = indicatorBitmap;
            invalidate();
        }
    }

    public void setPointer(int res) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
        setPointerBitmap(bitmap);
    }

    public void setPointerBitmap(Bitmap pointerBitmap) {
        if (mPointerBitmap != pointerBitmap) {
            mPointerBitmap = pointerBitmap;
            invalidate();
        }
    }

    public void setMarkerStepSize(float markerStepSize) {
        if (markerStepSize > 0 && mMarkerStepSize != markerStepSize) {
            mMarkerStepSize = markerStepSize;
            invalidate();
        }
    }

    public void setMarkerWidth(int markerWidth) {
        if (markerWidth > 0 && mMarkerWidth != markerWidth) {
            mMarkerWidth = markerWidth;
            mMarkerPaint.setStrokeWidth(markerWidth);
            invalidate();
        }
    }

    public void setMarkerColor(int markerColor) {
        if (mMarkerColor != markerColor) {
            mMarkerColor = markerColor;
            mMarkerPaint.setColor(markerColor);
            invalidate();
        }
    }

    public void setMarkerLength(int markerLength) {
        if (markerLength > 0 && mMarkerLength != markerLength) {
            mMarkerLength = markerLength;
            invalidate();
        }
    }

    public void setInnerRadiusColor(int innerRadiusColor) {
        if (mInnerRadiusColor != innerRadiusColor) {
            mInnerRadiusColor = innerRadiusColor;
            mInnerRadiusPaint.setColor(innerRadiusColor);
            invalidate();
        }
    }

    public void setInnerRadiusThickness(int innerRadiusThickness) {
        if (innerRadiusThickness > 0 && mInnerRadiusThickness != innerRadiusThickness) {
            mInnerRadiusThickness = innerRadiusThickness;
            mInnerRadiusPaint.setStrokeWidth(innerRadiusThickness);
            invalidate();
        }
    }

    public void setOuterRadiusColor(int outerRadiusColor) {
        if (mOuterRadiusColor != outerRadiusColor) {
            mOuterRadiusColor = outerRadiusColor;
            mOuterRadiusPaint.setColor(outerRadiusColor);
            invalidate();
        }
    }

    public void setOuterRadiusThickness(int outerRadiusThickness) {
        if (outerRadiusThickness > 0 && mOuterRadiusThickness != outerRadiusThickness) {
            mOuterRadiusThickness = outerRadiusThickness;
            mOuterRadiusPaint.setStrokeWidth(outerRadiusThickness);
            invalidate();
        }
    }
}
