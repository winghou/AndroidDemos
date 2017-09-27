package com.benio.demoproject.hencoder.practice1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice13DrawTextView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String mText = "Hello world!";

    public Practice13DrawTextView(Context context) {
        super(context);
    }

    public Practice13DrawTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice13DrawTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setTextSize(18);
        canvas.drawText(mText, 100, 25, mPaint);

        mPaint.setTextSize(36);
        canvas.drawText(mText, 100, 70, mPaint);

        mPaint.setTextSize(60);
        canvas.drawText(mText, 100, 145, mPaint);

        mPaint.setTextSize(84);
        canvas.drawText(mText, 100, 240, mPaint);
    }
}