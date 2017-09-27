package com.benio.demoproject.hencoder.practice1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice8DrawArcView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mRectF = new RectF(200, 100, 500, 400);

    public Practice8DrawArcView(Context context) {
        super(context);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawArc() 方法画弧形和扇形
        mPaint.setStyle(Paint.Style.FILL); // 填充模式
        canvas.drawArc(mRectF, 30, 60, true, mPaint); // 绘制扇形
        canvas.drawArc(mRectF, 100, 60, false, mPaint); // 绘制弧形

        mPaint.setStyle(Paint.Style.STROKE); // 画线模式
        canvas.drawArc(mRectF, 170, 60, true, mPaint);
        canvas.drawArc(mRectF, 240, 60, false, mPaint); // 绘制不封口的弧形
    }
}