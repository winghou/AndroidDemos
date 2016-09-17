package com.benio.demoproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B02%5DCanvas_BasicGraphics.md
 */
public class CustomView1 extends View {

    private Paint mPaint;

    public CustomView1(Context context) {
        super(context);
        init();
    }

    public CustomView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);     //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL);//设置画笔模式为填充
        mPaint.setStrokeWidth(10);        //设置画笔宽度为10px
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);

        // 绘制点
        canvas.drawPoint(100, 100, mPaint);//在坐标(100,100)位置绘制一个点
        canvas.drawPoints(new float[]{  //绘制一组点，坐标位置由float数组指定
                110, 120,
                110, 140,
                110, 160
        }, mPaint);

        // 绘制直线
        canvas.drawLine(200, 100, 200, 200, mPaint);// 在坐标(200,100)(200,200)之间绘制一条直线
        canvas.drawLines(new float[]{// 绘制一组线 每四数字(两个点的坐标)确定一条线
                300, 100, 300, 200,
                400, 100, 400, 200
        }, mPaint);

        // 绘制矩形
        canvas.drawRect(100, 250, 300, 350, mPaint);

        // 绘制圆角矩形
        RectF rectF1 = new RectF(400, 250, 600, 350);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rectF1, mPaint);
        mPaint.setColor(Color.GREEN);
        /*
         * 实际上在rx为宽度的一半，ry为高度的一半时，刚好是一个椭圆
         * 而当rx大于宽度的一半，ry大于高度的一半时，实际上是无法计算出圆弧的
         * 所以drawRoundRect对大于该数值的参数进行了限制(修正)，凡是大于一半的参数均按照一半来处理。
         */
        canvas.drawRoundRect(rectF1, 200, 100, mPaint);

        // 绘制椭圆
        RectF rectF2 = new RectF(100, 450, 300, 550);
        canvas.drawOval(rectF2, mPaint);

        // 绘制圆
        canvas.drawCircle(500, 500, 50, mPaint); // 绘制一个圆心坐标在(500,500)，半径为50 的圆。

        // 绘制圆弧
        RectF rectF3 = new RectF(100, 600, 300, 800);
        canvas.drawArc(rectF3, 0, 90, true, mPaint);

        RectF rectF4 = new RectF(400, 600, 600, 800);
        canvas.drawArc(rectF4, 0, 90, false, mPaint);

        // Paint Style
        mPaint.setStrokeWidth(30);     //为了实验效果明显，特地设置描边宽度非常大
        //填充
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(200, 950, 100, mPaint);
        //描边
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(450, 950, 100, mPaint);
        //描边加填充
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(700, 950, 100, mPaint);
    }
}