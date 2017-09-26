package com.benio.demoproject.badge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.graphics.drawable.DrawableWrapper;
import android.text.TextUtils;

public class BadgeDrawable extends DrawableWrapper {
    private boolean mVisible = true;
    private Paint mPaint;
    private Paint mTextPaint;
    private float mCornerRadius = 14;
    private String mText;
    private Rect mTextRect = new Rect();

    public BadgeDrawable(@NonNull Drawable drawable) {
        super(drawable);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(18);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public void setBadgeVisible(boolean badgeVisible) {
        mVisible = badgeVisible;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setCornerRadius(float cornerRadius) {
        mCornerRadius = cornerRadius;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        // do not change the wrapped drawable bounds
        // super.onBoundsChange(bounds);
    }

    @Override
    public int getIntrinsicHeight() {
        int height = super.getIntrinsicHeight();
        if (height != -1) {
            height += mCornerRadius;
        }
        return height;
    }

    @Override
    public int getIntrinsicWidth() {
        int width = super.getIntrinsicWidth();
        if (width != -1) {
            width += mCornerRadius;
        }
        return width;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.translate(mCornerRadius / 2, mCornerRadius);
        super.draw(canvas);
        canvas.restore();

        if (!mVisible) {
            return;
        }

        Rect bounds = getBounds();
        float cx = bounds.right - mCornerRadius;
        float cy = bounds.top + mCornerRadius;

        canvas.drawCircle(cx, cy, mCornerRadius, mPaint);

        final String text = mText;
        if (!TextUtils.isEmpty(text)) {
            mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
            canvas.drawText(text, cx, cy + mTextRect.height() / 2, mTextPaint);
        }
    }

    public static BadgeDrawable wrap(Drawable drawable) {
        if (drawable instanceof BadgeDrawable) {
            return (BadgeDrawable) drawable;
        }
        return drawable != null ? new BadgeDrawable(drawable) : null;
    }
}