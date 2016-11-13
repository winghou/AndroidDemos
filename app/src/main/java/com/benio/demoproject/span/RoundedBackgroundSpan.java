package com.benio.demoproject.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.LineBackgroundSpan;

//public class RoundedBackgroundSpan extends ReplacementSpan {
//
//
//    @Override
//    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
//        RectF rect = new RectF(x, top, x + measureText(paint, text, start, end), bottom);
//        paint.setColor(Color.BLUE);
//        canvas.drawRoundRect(rect, 100f, 30f, paint);
//        paint.setColor(Color.BLACK);
//        canvas.drawText(text, start, end, x, y, paint);
//    }
//
//    @Override
//    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
//        return Math.round(measureText(paint, text, start, end));
//    }
//
//    private float measureText(Paint paint, CharSequence text, int start, int end) {
//        return paint.measureText(text, start, end);
//    }
//
//}
public class RoundedBackgroundSpan implements LineBackgroundSpan {
    private int mBackgroundColor;
    private int mPadding;
    private Rect mBgRect;

    public RoundedBackgroundSpan(int backgroundColor, int padding) {
        super();
        mBackgroundColor = backgroundColor;
        mPadding = padding;
        // Precreate rect for performance
        mBgRect = new Rect();
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        final int textWidth = Math.round(p.measureText(text, start, end));
        final int paintColor = p.getColor();
        // Draw the background
        mBgRect.set(left - mPadding,
                top - (lnum == 0 ? mPadding / 2 : - (mPadding / 2)),
                left + textWidth + mPadding,
                bottom + mPadding / 2);
        p.setColor(mBackgroundColor);
        c.drawRect(mBgRect, p);
        p.setColor(paintColor);
    }
}