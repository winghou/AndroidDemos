package com.benio.demoproject.hencoder.practice3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

public class Practice17SetTextLocaleView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text = "雨骨底条今直沿微写";

    public Practice17SetTextLocaleView(Context context) {
        super(context);
    }

    public Practice17SetTextLocaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice17SetTextLocaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float fontSpacing = paint.getFontSpacing();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paint.setTextLocale(Locale.CHINA);
        }
        canvas.drawText(text, 50, 100, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paint.setTextLocale(Locale.TAIWAN);
        }
        canvas.drawText(text, 50, 100 + fontSpacing, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            paint.setTextLocale(Locale.JAPAN);
        }
        canvas.drawText(text, 50, 100 + fontSpacing * 2, paint);
    }
}