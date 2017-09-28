package com.benio.demoproject.hencoder.practice2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

public class Practice08XfermodeView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap1;
    Bitmap bitmap2;
    Xfermode mXfermode1 = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    Xfermode mXfermode2 = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    Xfermode mXfermode3 = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    public Practice08XfermodeView(Context context) {
        super(context);
    }

    public Practice08XfermodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice08XfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.batman);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.batman_logo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        // 使用 paint.setXfermode() 设置不同的结合绘制效果

        // 别忘了用 canvas.saveLayer() 开启 off-screen buffer
        canvas.drawBitmap(bitmap1, 0, 0, paint);
        // 第一个：PorterDuff.Mode.SRC
        paint.setXfermode(mXfermode1);
        canvas.drawBitmap(bitmap2, 0, 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmap1, bitmap1.getWidth(), 0, paint);
        // 第二个：PorterDuff.Mode.DST_IN
        paint.setXfermode(mXfermode2);
        canvas.drawBitmap(bitmap2, bitmap1.getWidth(), 0, paint);
        paint.setXfermode(null);

        canvas.drawBitmap(bitmap1, 0, bitmap1.getHeight(), paint);
        // 第三个：PorterDuff.Mode.DST_OUT
        paint.setXfermode(mXfermode3);
        canvas.drawBitmap(bitmap2, 0, bitmap1.getHeight(), paint);
        paint.setXfermode(null);

        // 用完之后使用 canvas.restore() 恢复 off-screen buffer
        canvas.restoreToCount(saveCount);
    }
}