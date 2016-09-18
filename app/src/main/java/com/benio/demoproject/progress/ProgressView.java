package com.benio.demoproject.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

/**
 * 圆环进度条
 * Created by zhangzhibin on 2016/9/13.
 */
public class ProgressView extends View {
    private int mProgress = 0;
    private int mMaxProgress = 100;

    private int mTextSize = 16;
    private int mTextColor = 0xFF000000;
    private Formatter mFormatter = new PercentFormatter();
    private Paint mTextPaint;
    private Rect mTextRect = new Rect();

    private Paint mReachedPaint;
    private Paint mUnreachedPaint;
    private RectF mRectF = new RectF();
    private int mThickness = 18;
    private float mStartAngle = 180;
    private float mSweepAngle = 180;
    private int mUnreachedColor = 0xFFCCCCCC;
    private Matrix mMatrix = new Matrix();
    private int[] mColors;

    private int mSize;
    private boolean mRefreshStartAngle = true;
    private boolean mTextInCenter = true;

    public ProgressView(Context context) {
        this(context, null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(context, attrs, defStyleAttr);
        initPainters();
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, 0);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case R.styleable.ProgressView_pv_progress:
                        mProgress = typedArray.getInt(attr, mProgress);
                        break;

                    case R.styleable.ProgressView_pv_max:
                        mMaxProgress = typedArray.getInt(attr, mMaxProgress);
                        break;

                    case R.styleable.ProgressView_pv_textSize:
                        mTextSize = typedArray.getDimensionPixelSize(attr, mTextSize);
                        break;

                    case R.styleable.ProgressView_pv_textColor:
                        mTextColor = typedArray.getColor(attr, mTextColor);
                        break;

                    case R.styleable.ProgressView_pv_thickness:
                        mThickness = typedArray.getDimensionPixelSize(attr, mThickness);
                        break;

                    case R.styleable.ProgressView_pv_startAngle:
                        mStartAngle = typedArray.getFloat(attr, mStartAngle);
                        break;

                    case R.styleable.ProgressView_pv_sweepAngle:
                        mSweepAngle = typedArray.getFloat(attr, mSweepAngle);
                        break;

                    case R.styleable.ProgressView_pv_unreachedColor:
                        mUnreachedColor = typedArray.getColor(attr, mUnreachedColor);
                        break;

                    case R.styleable.ProgressView_pv_textInCenter:
                        mTextInCenter = typedArray.getBoolean(attr, mTextInCenter);
                        break;
                }
            }
            typedArray.recycle();
        }
    }

    private void initPainters() {
        mReachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedPaint.setStyle(Paint.Style.STROKE);
        mReachedPaint.setStrokeWidth(mThickness);

        mUnreachedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedPaint.setColor(mUnreachedColor);
        mUnreachedPaint.setStyle(Paint.Style.STROKE);
        mUnreachedPaint.setStrokeWidth(mThickness);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String text = getText();
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);

        int width = mTextRect.width() + mThickness * 2;
        int height = mTextRect.height() + mThickness * 2;

        // 获取最大值作为view的初始
        int size = Math.max(width, height);
        int measuredWidth = resolveSizeAndState(size, widthMeasureSpec, 0);
        int measuredHeight = resolveSizeAndState(size, heightMeasureSpec, 0);

        // 获取最大值作为view的确定半径，防止View变形
        int result = Math.max(measuredHeight, measuredWidth);
        setMeasuredDimension(result + getPaddingLeft() + getPaddingRight(),
                result + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 去除padding对半径的影响
        w -= getPaddingLeft() + getPaddingRight();
        h -= getPaddingTop() + getPaddingBottom();
        // 重新计算圆周直径
        mSize = Math.min(w, h);
        // 更新矩形
        int left = mThickness;
        int top = mThickness;
        int right = mSize - left;
        int bottom = mSize - top;
        mRectF.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getPaddingLeft(), getPaddingTop());

        // Draw the text
        String text = getText();
        canvas.drawText(text,
                (mSize - mTextRect.width()) / 2f,
                // 文字位于圆中心。否则位于半圆之上
                mTextInCenter ? (mSize + mTextRect.height()) / 2f : mSize / 2f,
                mTextPaint);

        updateShaderAndMatrix();

        // Draw the arc
        float progressSweepAngle = mProgress / (float) mMaxProgress * mSweepAngle;
        canvas.drawArc(mRectF, mStartAngle, progressSweepAngle, false, mReachedPaint);
        canvas.drawArc(mRectF, mStartAngle + progressSweepAngle, mSweepAngle - progressSweepAngle, false, mUnreachedPaint);
    }

    private void updateShaderAndMatrix() {
        int radius = mSize / 2;
        if (mRefreshStartAngle) {
            //旋转 不然是从0度开始渐变
            mMatrix.reset();
            mMatrix.setRotate(mStartAngle, radius, radius);
            mRefreshStartAngle = false;
        }

        Shader shader = mReachedPaint.getShader();
        if (shader == null) {
            if (mColors != null) {
                shader = new SweepGradient(radius, radius, mColors, null);
                shader.setLocalMatrix(mMatrix);
                mReachedPaint.setShader(shader);
            }
        } else {
            shader.setLocalMatrix(mMatrix);
        }
    }

    public void setSweepAngle(float sweepAngle) {
        if (sweepAngle > 0 && mSweepAngle != sweepAngle) {
            mSweepAngle = sweepAngle;
            invalidate();
        }
    }

    public void setColors(int[] colors) {
        if (colors != null && colors != mColors) {
            mColors = colors;
            mReachedPaint.setShader(null);// 重新创建shader
            invalidate();
        }
    }

    public void setStartAngle(float startAngle) {
        if (mStartAngle != startAngle) {
            mStartAngle = startAngle;
            mRefreshStartAngle = true; // 标记startAngle需要刷新
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

    public void setUnreachedColor(int unreachedColor) {
        if (mUnreachedColor != unreachedColor) {
            mUnreachedColor = unreachedColor;
            mUnreachedPaint.setColor(unreachedColor);
            invalidate();
        }
    }

    public boolean isTextInCenter() {
        return mTextInCenter;
    }

    public void setTextInCenter(boolean textInCenter) {
        if (textInCenter != mTextInCenter) {
            mTextInCenter = textInCenter;
            invalidate();
        }
    }

    public String getText() {
        return mFormatter != null ? mFormatter.format(mProgress) : "";
    }

    public int getUnreachedColor() {
        return mUnreachedColor;
    }

    public Formatter getFormatter() {
        return mFormatter;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public int getThickness() {
        return mThickness;
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

    public int[] getColors() {
        return mColors;
    }
}
