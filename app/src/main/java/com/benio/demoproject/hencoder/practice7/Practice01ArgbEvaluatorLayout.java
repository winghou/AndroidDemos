package com.benio.demoproject.hencoder.practice7;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

public class Practice01ArgbEvaluatorLayout extends FrameLayout {
    Button animateBt;
    Practice01ArgbEvaluatorView view;

    public Practice01ArgbEvaluatorLayout(Context context) {
        super(context);
        init(context);
    }

    public Practice01ArgbEvaluatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice01ArgbEvaluatorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        view = new Practice01ArgbEvaluatorView(context);

        animateBt = new Button(context);
        animateBt.setText("animate");
        animateBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                animator.setInterpolator(new LinearInterpolator());
                // 在这里使用 ObjectAnimator.setEvaluator() 来设置 ArgbEvaluator，修复闪烁问题
                animator.setEvaluator(new ArgbEvaluator());
                animator.setDuration(2000);
                animator.start();
            }
        });
        addView(view);
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}