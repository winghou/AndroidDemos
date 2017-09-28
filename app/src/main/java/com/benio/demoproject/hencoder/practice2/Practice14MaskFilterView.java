package com.benio.demoproject.hencoder.practice2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.benio.demoproject.R;

public class Practice14MaskFilterView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;

    MaskFilter mMaskFilter1 = new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL);
    MaskFilter mMaskFilter2 = new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER);
    MaskFilter mMaskFilter3 = new BlurMaskFilter(50, BlurMaskFilter.Blur.OUTER);
    MaskFilter mMaskFilter4 = new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID);
    MaskFilter mMaskFilter5 = new EmbossMaskFilter(new float[]{0, 1, 1}, 0.5f, 8, 10);

    public Practice14MaskFilterView(Context context) {
        super(context);
    }

    public Practice14MaskFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice14MaskFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.what_the_fuck);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 用 Paint.setMaskFilter 来设置不同的 BlurMaskFilter

        // 第一个：NORMAL
        paint.setMaskFilter(mMaskFilter1);
        canvas.drawBitmap(bitmap, 100, 50, paint);

        // 第二个：INNER
        paint.setMaskFilter(mMaskFilter2);
        canvas.drawBitmap(bitmap, bitmap.getWidth() + 200, 50, paint);

        // 第三个：OUTER
        paint.setMaskFilter(mMaskFilter3);
        canvas.drawBitmap(bitmap, 100, bitmap.getHeight() + 100, paint);

        // 第四个：SOLID
        paint.setMaskFilter(mMaskFilter4);
        canvas.drawBitmap(bitmap, bitmap.getWidth() + 200, bitmap.getHeight() + 100, paint);

        paint.setMaskFilter(mMaskFilter5);
        canvas.drawBitmap(bitmap, 100, bitmap.getHeight() * 2 + 200, paint);
    }
}