package com.benio.demoproject.badge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.benio.demoproject.R;

/**
 * Created by zhangzhibin on 2017/11/14.
 */
public class BadgeRadioButton extends RadioButton {
    private boolean mBadgeVisible = true;
    private Paint mPaint;
    private int mRadius = 8;
    private int mRightMargin = 0;
    private int mTopMargin = 0;
    private boolean mClipToPadding = false;

    public BadgeRadioButton(Context context) {
        super(context);
        init(context, null);
    }

    public BadgeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BadgeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int badgeColor = Color.RED;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BadgeRadioButton);
        try {
            badgeColor = a.getColor(R.styleable.BadgeRadioButton_brb_color, badgeColor);
            mRadius = a.getDimensionPixelSize(R.styleable.BadgeRadioButton_brb_radius, mRadius);
            mTopMargin = a.getDimensionPixelSize(R.styleable.BadgeRadioButton_brb_topMargin, 0);
            mRightMargin = a.getDimensionPixelSize(R.styleable.BadgeRadioButton_brb_rightMargin, 0);
            mBadgeVisible = a.getBoolean(R.styleable.BadgeRadioButton_brb_visible, mBadgeVisible);
            mClipToPadding = a.getBoolean(R.styleable.BadgeRadioButton_brb_clipToPadding, mClipToPadding);
        } finally {
            a.recycle();
        }
        mPaint.setColor(badgeColor);
    }

    public void setBadgeColor(int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            invalidate();
        }
    }

    public void setBadgeRadius(int radius) {
        if (radius != mRadius) {
            mRadius = radius;
            invalidate();
        }
    }

    public void setBadgeTopMargin(int topMargin) {
        if (topMargin != mTopMargin) {
            mTopMargin = topMargin;
            invalidate();
        }
    }

    public void setBadgeRightMargin(int rightMargin) {
        if (rightMargin != mRightMargin) {
            mRightMargin = rightMargin;
            invalidate();
        }
    }

    public void setBadgeVisible(boolean visible) {
        if (visible != mBadgeVisible) {
            mBadgeVisible = visible;
            invalidate();
        }
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (mClipToPadding != clipToPadding) {
            mClipToPadding = clipToPadding;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mBadgeVisible) {
            return;
        }

        float cx, cy;
        if (mClipToPadding) {
            cx = getWidth() - getPaddingRight() - mRadius - mRightMargin;
            cy = getPaddingTop() + mRadius + mTopMargin;
        } else {
            cx = getWidth() - mRadius - mRightMargin;
            cy = mRadius + mTopMargin;
        }
        canvas.drawCircle(cx, cy, mRadius, mPaint);
    }
}
