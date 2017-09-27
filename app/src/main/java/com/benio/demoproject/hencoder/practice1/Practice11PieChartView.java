package com.benio.demoproject.hencoder.practice1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice11PieChartView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRectF = new RectF(100, 100, 500, 500);

    public Practice11PieChartView(Context context) {
        super(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(mRectF, 0, 60, true, mPaint);

        mPaint.setColor(Color.YELLOW);
        canvas.drawArc(mRectF, 70, 30, true, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawArc(mRectF, 100, 50, true, mPaint);

        mPaint.setColor(Color.LTGRAY);
        canvas.drawArc(mRectF, 155, 25, true, mPaint);

        mPaint.setColor(Color.GREEN);
        canvas.drawArc(mRectF, 180, 60, true, mPaint);

        mPaint.setColor(Color.MAGENTA);
        canvas.drawArc(mRectF, 240, 100, true, mPaint);
    }
}