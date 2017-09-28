package com.benio.demoproject.hencoder.practice2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice12PathEffectView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();

    PathEffect mPathEffect1 = new CornerPathEffect(30);
    PathEffect mPathEffect2 = new DiscretePathEffect(5, 10);
    PathEffect mPathEffect3 = new DashPathEffect(new float[]{15, 10, 5, 10}, 0);
    PathEffect mPathEffect4;
    PathEffect mPathEffect5 = new SumPathEffect(mPathEffect2, mPathEffect1);
    PathEffect mPathEffect6 = new ComposePathEffect(mPathEffect2, mPathEffect3);

    public Practice12PathEffectView(Context context) {
        super(context);
    }

    public Practice12PathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice12PathEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStyle(Paint.Style.STROKE);

        path.moveTo(50, 100);
        path.rLineTo(50, 100);
        path.rLineTo(80, -150);
        path.rLineTo(100, 100);
        path.rLineTo(70, -120);

        Path dashPath = new Path();
        dashPath.lineTo(10, -20);
        dashPath.lineTo(20, 0);
        dashPath.close();
        mPathEffect4 = new PathDashPathEffect(dashPath, 30, 0, PathDashPathEffect.Style.TRANSLATE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 Paint.setPathEffect() 来设置不同的 PathEffect

        // 第一处：CornerPathEffect
        paint.setPathEffect(mPathEffect1);
        canvas.drawPath(path, paint);

        canvas.save();
        canvas.translate(350, 0);
        // 第二处：DiscretePathEffect
        paint.setPathEffect(mPathEffect2);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 200);
        // 第三处：DashPathEffect
        paint.setPathEffect(mPathEffect3);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(350, 200);
        // 第四处：PathDashPathEffect
        paint.setPathEffect(mPathEffect4);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 400);
        // 第五处：SumPathEffect
        paint.setPathEffect(mPathEffect5);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(350, 400);
        // 第六处：ComposePathEffect
        paint.setPathEffect(mPathEffect6);
        canvas.drawPath(path, paint);
        canvas.restore();
    }
}