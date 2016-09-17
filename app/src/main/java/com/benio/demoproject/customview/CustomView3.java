package com.benio.demoproject.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

/**
 * https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B04%5DCanvas_PictureText.md
 * Created by benio on 2016/9/15.
 */
public class CustomView3 extends View {
    private Paint mPaint;
    private Paint mTextPaint;
    private Picture mPicture = new Picture();

    public CustomView3(Context context) {
        super(context);
        init();
    }

    public CustomView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        // 开始录制 (接收返回值Canvas)
        Canvas canvas = mPicture.beginRecording(500, 500);
        canvas.translate(250, 250);
        canvas.drawCircle(0, 0, 100, mPaint);
        mPicture.endRecording();

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //	1.使用Picture提供的draw方法绘制。
        // 在比较低版本的系统上绘制后可能会影响Canvas状态(Matrix clip等)，所以这种方法一般不会使用。
        mPicture.draw(canvas);

        canvas.translate(0, 300);

        // 2.使用Canvas提供的drawPicture方法绘制。
        // 不影响Canvas的状态
        // 绘制的内容根据选区进行了缩放
        canvas.drawPicture(mPicture, new RectF(0, 0, mPicture.getWidth(), 200));

        canvas.translate(0, 100);

        // 3.将Picture包装成为PictureDrawable，使用PictureDrawable的draw方法绘制。
        // 不影响Canvas的状态
        // 包装成为Drawable
        PictureDrawable drawable = new PictureDrawable(mPicture);
        // 设置绘制区域 -- 注意此处所绘制的实际内容不会缩放
        drawable.setBounds(0, 0, 250, mPicture.getHeight());
        // 绘制
        drawable.draw(canvas);

        canvas.translate(0, 100);

        //drawBitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 第一种方法中后两个参数(matrix, paint)是在绘制的时候对图片进行一些改变
        canvas.drawBitmap(bitmap, new Matrix(), null);
        // 第二种方法就是在绘制时指定了图片左上角的坐标(距离坐标原点的距离)：
        canvas.drawBitmap(bitmap, 200, 200, null);
        // 第三种方法指定图片绘制区域
        canvas.translate(200, 200);
        // 指定图片绘制区域(左上角的四分之一)
        Rect src = new Rect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        // 指定图片在屏幕上显示的区域
        Rect dst = new Rect(0, 0, 200, 400);
        // 绘制图片
        canvas.drawBitmap(bitmap, src, dst, null);

        // 绘制文字
        String str = "ABCDEFGHI";
        // 第一类(drawText)
        // 参数分别为 (文本 基线x 基线y 画笔)
        canvas.drawText(str, -150, 500, mTextPaint);
        // 参数分别为 (字符串 开始截取位置 结束截取位置 基线x 基线y 画笔)
        canvas.drawText(str, 1, 3, -150, 550, mTextPaint); //"BC"
        char[] chars = str.toCharArray();
        // 参数为 (字符数组 起始坐标 截取长度 基线x 基线y 画笔)
        canvas.drawText(chars, 3, 5, -150, 600, mTextPaint);//"DEFGH"

        // 第二类(drawPosText)
        String str2 = "QWE";
        canvas.drawPosText(str2, new float[]{
                150, 500,
                180, 550,
                210, 600
        }, mTextPaint);
    }
}
