package com.benio.demoproject.hencoder.practice1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Practice10HistogramView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRectF = new RectF();

    public Practice10HistogramView(Context context) {
        super(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        int beginX = 50;
        int width = 50;
        int offset = 30;
        int bottomY = 600;
        int count = 6;
        int step = 80;
        for (int i = 0; i < count; i++) {
            mRectF.set(beginX, bottomY - step * (i + 1), beginX + width, bottomY);
            Log.i("xxx", "onDraw: " + mRectF);
            canvas.drawRect(mRectF, mPaint);
            beginX = offset + beginX + width;
        }
    }
}