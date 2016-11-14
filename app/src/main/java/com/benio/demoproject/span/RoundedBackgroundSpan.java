package com.benio.demoproject.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;
import android.util.Log;

public class RoundedBackgroundSpan extends ReplacementSpan {
    private static final String TAG = "xxxx";
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;

    private int mMarginLeft;
    private int mMarginTop;
    private int mMarginRight;
    private int mMarginBottom;

    private RectF mRectF;
    private int mBackgroundColor;
    private int mRadius;
    private Paint mPaint;
    private boolean[] mIsDigitArray;
    private int mSpanWidth = -1;

    public RoundedBackgroundSpan() {
        mRadius = 10;
        mMarginLeft = mMarginRight = 8;
        mPaddingLeft = mPaddingRight = 10;
        mBackgroundColor = Color.YELLOW;
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mBackgroundColor);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (mSpanWidth < 0) {
            String subString = text.subSequence(start, end).toString();
            boolean[] digit = new boolean[subString.length()];
            int horizontalPadding = mPaddingLeft + mPaddingRight;
            int horizontalMargin = mMarginLeft + mMarginRight;
            int digitNum = 0;
            for (int i = start; i < end; i++) {
                char charAt = subString.charAt(i);
                boolean isDigit = Character.isDigit(charAt);
                digit[i] = isDigit;
                if (isDigit) {
                    digitNum++;
                }
            }
            mIsDigitArray = digit;
            mSpanWidth = (int) paint.measureText(text, start, end) + digitNum * horizontalPadding + (digitNum - 1) * horizontalMargin;
        }
        Log.d(TAG, "getSize: " + mSpanWidth + "," + start + "," + end);
        return mSpanWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Log.d(TAG, "draw() called with: canvas = [" + canvas + "], text = [" + text + "], start = [" + start + "], end = [" + end + "], x = [" + x + "], top = [" + top + "], y = [" + y + "], bottom = [" + bottom + "], paint = [" + paint + "]");
//        float charx = x;
//        char[] chars = text.subSequence(start, end).toString().toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            final char charAt = chars[i];
//            float charWidth = paint.measureText(chars, i, 1);
//            mRectF.left = charx + mMarginLeft;
//            mRectF.top = top;
//            mRectF.right = mRectF.left + charWidth + mPaddingLeft + mPaddingRight;
//            mRectF.bottom = bottom;
//            if (Character.isDigit(charAt)) {
//                canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
//            } else {
//                // 去除上一个字符多加的padding
//                mRectF.left = charx + mMarginLeft - mPaddingLeft - mPaddingRight;
//                mRectF.right = mRectF.left + charWidth;
//            }
//
//            canvas.drawText(String.valueOf(charAt), 0, 1, mRectF.left + mPaddingLeft, y, paint);
//            charx = mRectF.right + mMarginRight;
//            Log.d(TAG, "draw: " + charWidth + "," + mRectF);
//        }

        String subString = text.subSequence(start, end).toString();
        float charx = x;
        for (int i = start; i < end; i++) {

        }

    }

    private static String extractText(CharSequence text, int start, int end) {
        return text.subSequence(start, end).toString();
    }
}