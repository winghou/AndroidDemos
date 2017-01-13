package com.benio.demoproject.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;
import android.util.Log;

/**
 * 用于绘制格式如：123,456,789的textView背景的Span
 * Created by benio on 2016/11/14.
 */
public class RoundedBackgroundSpan extends ReplacementSpan {
    private static final String TAG = "RoundedBackgroundSpan";
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
    private Rect mTextRect;

    public RoundedBackgroundSpan() {
        this(0, 0, 0, 0);
    }

    /**
     * @param backgroundColor 数字的背景颜色
     * @param radius          数字背景弧度
     * @param padding         数字内边距
     * @param margin          数字外边距
     */
    public RoundedBackgroundSpan(int backgroundColor, int radius, int padding, int margin) {
        mRadius = radius;
        mMarginLeft = mMarginRight = mMarginTop = mMarginBottom = margin;
        mPaddingLeft = mPaddingRight = mPaddingTop = mPaddingBottom = padding;
        mBackgroundColor = backgroundColor;
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mBackgroundColor);
        mTextRect = new Rect();
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        // 计算text中有多少个数字
        // 只有数字才会绘制背景
        int digitNum = 0;
        for (int i = start; i < end; i++) {
            char charAt = text.charAt(i);
            if (Character.isDigit(charAt)) {
                digitNum++;
            }
        }
        paint.getTextBounds(text.toString(), start, end, mTextRect);
        // Span宽度 = 字体宽度 + 数字数量 * (padding + margin)
        int width = mTextRect.width() + digitNum * (mPaddingLeft + mPaddingRight + mMarginLeft + mMarginRight);
        Log.i(TAG, "getSize() called with:" + "text = [" + text + "], start = [" + start + "], end = [" + end + "]" + fm);
        if (fm != null) {
            fm.ascent = -mTextRect.height();
            fm.descent = mTextRect.bottom;
            fm.top = fm.ascent;
            fm.bottom = fm.descent;
        }
        Log.i(TAG, "fm:" + fm + "," + mTextRect.width() + "," + mTextRect.height() + ", " + mTextRect);
        return width;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Log.i(TAG, "draw() called with : text = [" + text + "], start = [" + start + "], end = [" + end + "], x = [" + x + "], top = [" + top + "], y = [" + y + "], bottom = [" + bottom + "]" + paint.getFontMetricsInt());
        float charx = x;
        for (int i = start; i < end; i++) {
            final char charAt = text.charAt(i);
            final String charStr = String.valueOf(charAt);
            float charWidth = paint.measureText(charStr);
            boolean isDigit = Character.isDigit(charAt);

            // 绘制数字的背景
            if (isDigit) {
                mRectF.set(charx + mMarginLeft,
                        top,
                        charx + mMarginLeft + charWidth + mPaddingLeft + mPaddingRight,
                        bottom);
                canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
            }

            // 计算绘制字符的位置，如果是数字的话要加上padding和margin，否则忽略padding和margin
            charx = charx + (isDigit ? mPaddingLeft + mMarginLeft : 0);
            // 画出每个字符
            canvas.drawText(charStr, 0, 1, charx, y, paint);
            //Log.d(TAG, "draw: " + charAt + ", " + charWidth + ", " + charx + ", " + mRectF);
            // 计算下一个字符的位置
            charx = charx + charWidth + (isDigit ? mPaddingRight + mMarginRight : 0);
        }
    }
}