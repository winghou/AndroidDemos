package com.benio.demoproject.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by benio on 2016/9/11.
 */
public class PieView extends View {
    // 颜色表(注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private List<PieData> mPieDatas;
    // 宽高
    private int mHeight, mWidth;
    // 画笔
    private Paint mPaint;

    private RectF mRectF = new RectF();

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPieDatas == null) {
            return;
        }
        float currentStartAngle = mStartAngle; // 当前起始角度
        canvas.translate(mWidth >> 1, mHeight >> 1);// 将画布坐标原点移动到中心位置
        float radius = Math.min(mWidth, mHeight) >> 1;// 饼状图半径
        mRectF.set(-radius, -radius, radius, radius); // 饼状图绘制区域

        for (int i = 0, count = mPieDatas.size(); i < count; i++) {
            PieData pie = mPieDatas.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(mRectF, currentStartAngle, pie.getAngle(), true, mPaint);
            currentStartAngle += pie.getAngle();
        }
    }

    // 设置起始角度
    public void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
        invalidate();
    }

    public void setPieDatas(List<PieData> pieDatas) {
        this.mPieDatas = pieDatas;
        calculatePieDatas(pieDatas);
        invalidate();
    }

    private void calculatePieDatas(List<PieData> pieDatas) {
        if (pieDatas == null || pieDatas.size() == 0) {
            return;
        }
        float sumValue = 0;
        for (int i = 0, count = pieDatas.size(); i < count; i++) {
            PieData pieData = mPieDatas.get(i);
            sumValue += pieData.getValue();//计算数值和
            int j = i % mColors.length;//设置颜色
            pieData.setColor(mColors[j]);
        }

        for (int i = 0, count = pieDatas.size(); i < count; i++) {
            PieData pieData = mPieDatas.get(i);
            float percent = pieData.getValue() / sumValue;// 百分比
            float angle = percent * 360; // 对应的角度
            pieData.setAngle(angle);
            pieData.setPercentage(percent);
        }
    }

    public static class PieData {
        // 用户关心数据
        private String name;        // 名字
        private float value;        // 数值
        private float percentage;   // 百分比

        // 非用户关心数据
        private int color = 0;      // 颜色
        private float angle = 0;    // 角度

        public PieData(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }
}
