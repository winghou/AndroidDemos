package com.benio.demoproject.hencoder.practice3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class Practice19BreakTextView extends View {
    TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello HenCoder";

    public Practice19BreakTextView(Context context) {
        super(context);
    }

    public Practice19BreakTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice19BreakTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float fontSpacing = paint.getFontSpacing();
        int measuredCount;
        float[] measuredWidth = {0};

// 宽度上限 300 （不够用，截断）
        measuredCount = paint.breakText(text, 0, text.length(), true, 300, measuredWidth);
        canvas.drawText(text, 0, measuredCount, 150, 150, paint);

// 宽度上限 400 （不够用，截断）
        measuredCount = paint.breakText(text, 0, text.length(), true, 400, measuredWidth);
        canvas.drawText(text, 0, measuredCount, 150, 150 + fontSpacing, paint);

// 宽度上限 500 （够用）
        measuredCount = paint.breakText(text, 0, text.length(), true, 500, measuredWidth);
        canvas.drawText(text, 0, measuredCount, 150, 150 + fontSpacing * 2, paint);

// 宽度上限 600 （够用）
        measuredCount = paint.breakText(text, 0, text.length(), true, 600, measuredWidth);
        canvas.drawText(text, 0, measuredCount, 150, 150 + fontSpacing * 3, paint);
    }
}