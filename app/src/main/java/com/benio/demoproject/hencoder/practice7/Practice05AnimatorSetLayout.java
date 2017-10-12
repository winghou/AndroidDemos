package com.benio.demoproject.hencoder.practice7;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.benio.demoproject.R;

public class Practice05AnimatorSetLayout extends LinearLayout {
    View view;
    Button animateBt;

    public Practice05AnimatorSetLayout(Context context) {
        super(context);
        init(context);
    }

    public Practice05AnimatorSetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice05AnimatorSetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.music);
        view = imageView;
        animateBt = new Button(context);
        animateBt.setText("animate");
        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setTranslationX(0);

                ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationX", 0, 400);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "rotation", 0, 1080);
                animator3.setDuration(2000);

                AnimatorSet animatorSet = new AnimatorSet();
                // 用 AnimatorSet 的方法来让三个动画协作执行
                // 要求 1： animator1 先执行，animator2 在 animator1 完成后立即开始
                // 要求 2： animator2 和 animator3 同时开始
                animatorSet.play(animator1).before(animator2);
                animatorSet.playTogether(animator2, animator3);
                animatorSet.start();
            }
        });
        addView(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}