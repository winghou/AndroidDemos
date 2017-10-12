package com.benio.demoproject.hencoder.practice7;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.benio.demoproject.R;

public class Practice04PropertyValuesHolderLayout extends LinearLayout {
    View view;
    Button animateBt;

    public Practice04PropertyValuesHolderLayout(Context context) {
        super(context);
        init(context);
    }

    public Practice04PropertyValuesHolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice04PropertyValuesHolderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
                // 使用 PropertyValuesHolder.ofFloat() 来创建不同属性的动画值方案
                // 第一个： scaleX 从 0 到 1
                // 第二个： scaleY 从 0 到 1
                // 第三个： alpha 从 0 到 1

                // 然后，用 ObjectAnimator.ofPropertyValuesHolder() 把三个属性合并，创建 Animator 然后执行

                PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
                PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
                PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("alpha", 0, 1);

                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, holder1, holder2, holder3);
                animator.start();
            }
        });
        addView(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}