package com.benio.demoproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

/**
 * Created by zhangzhibin on 2017/6/22.
 */
public class TimelineView extends View {
    public static final int LINE_STYLE_NONE = -1;
    public static final int LINE_STYLE_BOTH = 0;
    public static final int LINE_STYLE_TOP = 1;
    public static final int LINE_STYLE_BOTTOM = 2;

    private Paint mIndicatorPaint;
    private int mIndicatorSize = 10;
    private int mIndicatorPadding = 0;
    private Paint mLinePaint;
    private int mLineWidth = 2;
    private int mLineStyle = LINE_STYLE_BOTH;

    public TimelineView(Context context) {
        super(context);
        init(context, null);
    }

    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimelineView);
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.TimelineView_tl_lineWidth, mLineWidth);
        mIndicatorSize = typedArray.getDimensionPixelSize(R.styleable.TimelineView_tl_indicatorSize, mIndicatorSize);
        mIndicatorPadding = typedArray.getDimensionPixelSize(R.styleable.TimelineView_tl_indicatorPadding, 0);
        mLineStyle = typedArray.getInt(R.styleable.TimelineView_tl_lineStyle, mLineStyle);
        int lineColor = typedArray.getColor(R.styleable.TimelineView_tl_lineColor, Color.BLACK);
        int indicatorColor = typedArray.getColor(R.styleable.TimelineView_tl_indicatorColor, Color.BLACK);
        typedArray.recycle();

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(indicatorColor);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(lineColor);
        mLinePaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int width = getWidth();
        final int height = getHeight();

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        canvas.translate(paddingLeft, paddingTop);
        // draw indicator
        float radius = mIndicatorSize / 2.0f;
        float cx = (width - paddingLeft - paddingRight) / 2.0f;
        float cy = mIndicatorPadding + radius;
        canvas.drawCircle(cx, cy, radius, mIndicatorPaint);

        // draw line
        boolean drawTopLine, drawBottomLine;

        switch (mLineStyle) {
            case LINE_STYLE_BOTH:
                drawTopLine = mIndicatorPadding > 0;
                drawBottomLine = true;
                break;

            case LINE_STYLE_TOP:
                drawTopLine = mIndicatorPadding > 0;
                drawBottomLine = false;
                break;
            case LINE_STYLE_BOTTOM:
                drawTopLine = false;
                drawBottomLine = true;
                break;
            case LINE_STYLE_NONE:
            default:
                drawTopLine = false;
                drawBottomLine = false;
                break;
        }

        if (drawTopLine) {
            canvas.drawLine(cx, 0, cx, mIndicatorPadding, mLinePaint);
        }
        if (drawBottomLine) {
            canvas.drawLine(cx, mIndicatorPadding + mIndicatorSize, cx, height - paddingBottom, mLinePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = Math.max(mIndicatorSize, mLineWidth) + getPaddingLeft() + getPaddingRight();
        final int minimumHeight = mIndicatorPadding + mIndicatorSize + getPaddingTop() + getPaddingBottom();
        int viewWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int viewHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private static int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    public void setLineWidth(int lineWidth) {
        if (mLineWidth != lineWidth) {
            int oldValue = mLineWidth;
            mLineWidth = lineWidth;
            mLinePaint.setStrokeWidth(lineWidth);
            if (Math.max(mIndicatorSize, oldValue) >= lineWidth) {
                invalidate();
            } else {
                requestLayout();
            }
        }
    }

    public int getLineWidth() {
        return mLineWidth;
    }

    public void setLineColor(int color) {
        if (mLinePaint.getColor() != color) {
            mLinePaint.setColor(color);
            invalidate();
        }
    }

    public int getLineColor() {
        return mLinePaint.getColor();
    }

    public void setIndicatorSize(int indicatorSize) {
        if (mIndicatorSize != indicatorSize) {
            int oldValue = mIndicatorSize;
            mIndicatorSize = indicatorSize;
            if (Math.max(mLineWidth, oldValue) >= indicatorSize) {
                invalidate();
            } else {
                requestLayout();
            }
        }
    }

    public void setIndicatorColor(int color) {
        if (mIndicatorPaint.getColor() != color) {
            mIndicatorPaint.setColor(color);
            invalidate();
        }
    }

    public int getIndicatorColor() {
        return mIndicatorPaint.getColor();
    }

    public void setIndicatorPadding(int indicatorPadding) {
        if (indicatorPadding < 0) {
            return;
        }
        if (mIndicatorPadding != indicatorPadding) {
            mIndicatorPadding = indicatorPadding;
            requestLayout();
        }
    }

    public int getIndicatorPadding() {
        return mIndicatorPadding;
    }

    public void setLineStyle(int lineStyle) {
        switch (lineStyle) {
            case LINE_STYLE_BOTH:
            case LINE_STYLE_BOTTOM:
            case LINE_STYLE_TOP:
            case LINE_STYLE_NONE:
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (mLineStyle != lineStyle) {
            mLineStyle = lineStyle;
            invalidate();
        }
    }

    public int getLineStyle() {
        return mLineStyle;
    }
}
