package com.benio.demoproject.hencoder.practice3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class Practice18MeasureTextView extends View {
    TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello HenCoder";
    float total;

    public Practice18MeasureTextView(Context context) {
        super(context);
    }

    public Practice18MeasureTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice18MeasureTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);

        /*
         * getTextWidths(), 获取字符串中每个字符的宽度，并把结果填入参数 widths
         * 这相当于 measureText() 的一个快捷方法，
         * 它的计算等价于对字符串中的每个字符分别调用 measureText() ，并把它们的计算结果分别填入 widths 的不同元素。
         */
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        for (int i = 0, count = widths.length; i < count; i++) {
            total += widths[i];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = 50;
        int y = 100;
        float fontSpacing = paint.getFontSpacing();
        float textWidth = paint.measureText(text);
        canvas.drawText(text, x, y, paint);
        canvas.drawLine(x, y, x + textWidth, y, paint);

        canvas.drawText("measureText(): " + textWidth, x, y + fontSpacing, paint);
        canvas.drawText("getTextWidths()：" + total, x, y + fontSpacing * 2, paint);
    }
}