package com.benio.demoproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B03%5DCanvas_Convert.md
 */
public class CustomView2 extends View {
    private Paint mPaint;

    public CustomView2(Context context) {
        super(context);
        init();
    }

    public CustomView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 位移
        canvas.save();
        canvas.translate(100, 100); //位移是基于当前位置移动
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(0, 0, 100, mPaint);

        canvas.translate(250, 0); //右移
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(0, 0, 100, mPaint);
        canvas.restore();

        canvas.translate(0, 250);// 换行

        // 缩放
        RectF rect1 = new RectF(0, 0, 200, 200);   // 矩形区域
        canvas.save();
        mPaint.setColor(Color.BLACK);// 绘制黑色矩形
        canvas.drawRect(rect1, mPaint);
        canvas.scale(0.5f, 0.5f); // 画布缩放
        mPaint.setColor(Color.BLUE); // 绘制蓝色矩形
        canvas.drawRect(rect1, mPaint);
        canvas.restore();

        canvas.translate(250, 0);//右移

        canvas.save();
        mPaint.setColor(Color.BLACK);// 绘制黑色矩形
        canvas.drawRect(rect1, mPaint);
        canvas.scale(0.5f, 0.5f, 100, 100);// 画布缩放, <-- 缩放中心向右偏移了100个单位
        mPaint.setColor(Color.BLUE);// 绘制蓝色矩形
        canvas.drawRect(rect1, mPaint);
        canvas.restore();

        canvas.translate(-25, 450);// 换行

        mPaint.setStyle(Paint.Style.STROKE);

        // 旋转
        canvas.save();
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(rect1, mPaint);
        canvas.rotate(180);// 旋转180度 <-- 默认旋转中心为原点
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect1, mPaint);
        canvas.restore();

        canvas.translate(450, 0);// 右移

        canvas.save();
        mPaint.setColor(Color.BLACK);
        canvas.drawRect(rect1, mPaint);
        canvas.rotate(60, 100, 0);// 旋转180度 <-- 旋转中心向右偏移200个单位
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(rect1, mPaint);
        canvas.restore();

        canvas.translate(-670, 250);// 换行

        // 错切
        canvas.save();
        mPaint.setColor(Color.BLACK);           // 绘制黑色矩形
        canvas.drawRect(rect1, mPaint);
        canvas.skew(1, 0);                       // 水平错切 <- 45度
        mPaint.setColor(Color.BLUE);            // 绘制蓝色矩形
        canvas.drawRect(rect1, mPaint);
        canvas.restore();
    }
}
