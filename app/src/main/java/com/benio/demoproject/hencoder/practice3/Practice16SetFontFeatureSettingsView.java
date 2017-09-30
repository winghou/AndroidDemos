package com.benio.demoproject.hencoder.practice3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice16SetFontFeatureSettingsView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text = "Hello HenCoder";

    public Practice16SetFontFeatureSettingsView(Context context) {
        super(context);
    }

    public Practice16SetFontFeatureSettingsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice16SetFontFeatureSettingsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paint.setFontFeatureSettings("smcp");
        }
        canvas.drawText(text, 50, 100, paint);
    }
}