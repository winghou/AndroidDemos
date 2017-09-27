package com.benio.demoproject.hencoder.practice1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

public class Practice12DrawBitmapView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;

    public Practice12DrawBitmapView(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    public Practice12DrawBitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice12DrawBitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 300, 300, mPaint);
    }
}