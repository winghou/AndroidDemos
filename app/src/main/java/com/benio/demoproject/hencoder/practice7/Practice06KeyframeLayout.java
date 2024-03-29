package com.benio.demoproject.hencoder.practice7;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class Practice06KeyframeLayout extends LinearLayout {
    Practice06KeyframeView view;
    Button animateBt;

    public Practice06KeyframeLayout(Context context) {
        super(context);
        init(context);
    }

    public Practice06KeyframeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice06KeyframeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        view = new Practice06KeyframeView(context);
        animateBt = new Button(context);
        animateBt.setText("animate");
        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Keyframe.ofFloat() 来为 view 的 progress 属性创建关键帧
                // 初始帧：progress 为 0
                // 时间进行到一般：progress 为 100
                // 结束帧：progress 回落到 80
                // 使用 PropertyValuesHolder.ofKeyframe() 来把关键帧拼接成一个完整的属性动画方案
                // 使用 ObjectAnimator.ofPropertyValuesHolder() 来创建动画
                Keyframe keyframe1 = Keyframe.ofFloat(0, 0);
                Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100);
                Keyframe keyframe3 = Keyframe.ofFloat(1, 80);
                PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofKeyframe("progress", keyframe1, keyframe2, keyframe3);
                ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolder);
                animator.setDuration(2000);
                animator.start();
            }
        });
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}