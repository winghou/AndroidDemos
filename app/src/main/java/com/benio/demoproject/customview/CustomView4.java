package com.benio.demoproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B05%5DPath_Basic.md
 * Created by benio on 2016/9/15.
 */
public class CustomView4 extends View {
    private static final String TAG = "CustomView4";
    private Paint mPaint;

    public CustomView4(Context context) {
        super(context);
        init();
    }

    public CustomView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // lineTo
        Path path1 = new Path();
        path1.lineTo(100, 100);
        path1.lineTo(0, 100);

        // moveTo 移动下一次操作的起点位置
        path1.moveTo(200, 0);
        path1.lineTo(200, 100);

        // setLastPoint 设置之前操作的最后一个点位置
        path1.moveTo(300, 0);
        path1.lineTo(400, 100);
        path1.setLastPoint(400, 50);
        path1.lineTo(300, 100);

        // close 连接当前最后一个点和最初的一个点
        // 注意：close的作用是封闭路径，与当前最后一个点和第一个点并不等价。
        // 如果连接了最后一个点和第一个点仍然无法形成封闭图形，则close什么 也不做。
        path1.moveTo(500, 0);
        path1.lineTo(600, 50);
        path1.lineTo(500, 100);
        path1.close();

        path1.moveTo(700, 0);
        path1.lineTo(800, 50);
        path1.moveTo(800, 0);
        path1.lineTo(800, 100);
        path1.close();
        canvas.drawPath(path1, mPaint);

        canvas.translate(50, 200);

        // addPath
        Path path2 = new Path();
        path2.addRect(0, 0, 100, 100, Path.Direction.CW); //顺时针
        path2.addRect(150, 0, 250, 100, Path.Direction.CW);
        path2.setLastPoint(150, 50); //重置最后一个点的位置

        Path tmpPath = new Path();
        path2.addRect(300, 0, 500, 200, Path.Direction.CW);
        tmpPath.addCircle(400, 100, 100, Path.Direction.CW);
        path2.addPath(tmpPath, 100, 100); // 添加到path2之前进行平移

        canvas.drawPath(path2, mPaint);

        canvas.translate(0, 350);

        // addArc与arcTo
        Path path3 = new Path();
        path3.lineTo(100, 100);
        RectF oval1 = new RectF(0, 0, 200, 200);
        path3.addArc(oval1, 0, 270);
        //path3.arcTo(oval,0,270,true);             // <-- 和上面一句作用等价
        canvas.drawPath(path3, mPaint);

        canvas.translate(0, 300);

        Path path4 = new Path();
        path4.lineTo(100, 100);
        RectF oval2 = new RectF(0, 0, 200, 200);
        path4.arcTo(oval2, 0, 270);
        //path4.arcTo(oval2,0,270, false);             // <-- 和上面一句作用等价

        canvas.drawPath(path4, mPaint);

        // isEmpty
        Path path5 = new Path();
        Log.d(TAG, "path5.isEmpty: " + path5.isEmpty());
        path5.lineTo(0, 0);
        Log.d(TAG, "path5.isEmpty: " + path5.isEmpty());
        path5.lineTo(100, 100);
        Log.d(TAG, "path5.isEmpty: " + path5.isEmpty());

        path5.reset();

        // isRect
        path5.lineTo(0, 400);
        path5.lineTo(400, 400);
        path5.lineTo(400, 0);
        path5.lineTo(0, 0);
        RectF rect = new RectF();
        Log.d(TAG, "path5.isEmpty: " + path5.isRect(rect) + "| left:" + rect.left + "| top:" + rect.top + "| right:" + rect.right + "| bottom:" + rect.bottom);

        path5.reset();

        canvas.translate(0, 300);
        //set
        path5.addRect(0, 0, 200, 200, Path.Direction.CW);
        Path tmpPath2 = new Path();
        tmpPath2.lineTo(100, 100);
        tmpPath2.addCircle(100, 100, 100, Path.Direction.CW);
        path5.set(tmpPath2); // 大致相当于 path = src;

        canvas.drawPath(path5, mPaint);

        canvas.translate(300, 0);

        // offset
        Path path6 = new Path();
        path6.addCircle(100, 100, 100, Path.Direction.CW);
        Path tmpPath3 = new Path();
        tmpPath3.addRect(0, 0, 200, 200, Path.Direction.CW);
        path6.offset(100, 100, tmpPath3); // 将当前path平移后的状态存入tmpPath3中，不会影响当前path

        canvas.drawPath(path6, mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(tmpPath3, mPaint);
    }
}
