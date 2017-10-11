package com.benio.demoproject.hencoder.practice6;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.benio.demoproject.common.utils.ScreenUtils;

public class Practice08ObjectAnimatorLayout extends LinearLayout {
    Practice08ObjectAnimatorView view;
    Button animateBt;

    public Practice08ObjectAnimatorLayout(Context context) {
        super(context);
        init(context);
    }

    public Practice08ObjectAnimatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice08ObjectAnimatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        view = new Practice08ObjectAnimatorView(context);
        animateBt = new Button(context);
        animateBt.setText("animate");
        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 在这里处理点击事件，用 ObjectAnimator 播放动画
                // 1. 用 ObjectAnimator 创建 Animator 对象
                // 2. 用 start() 执行动画
                // *. 记得在 Practice08ObjectAnimatorView 中为 progress 添加 setter/ getter 方法！
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", 0, 65);
                animator.setDuration(1000);
                animator.start();
            }
        });
        addView(view, (int) ScreenUtils.dpToPx(400), (int) ScreenUtils.dpToPx(400));
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}