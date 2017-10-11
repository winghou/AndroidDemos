package com.benio.demoproject.hencoder.practice6;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.benio.demoproject.R;

public class Practice05MultiProperties extends LinearLayout {
    Button animateBt;
    ImageView imageView;

    public Practice05MultiProperties(Context context) {
        super(context);
        init(context);
    }

    public Practice05MultiProperties(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice05MultiProperties(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.music);
        animateBt = new Button(context);
        animateBt.setText("animate");
        animateBt.setOnClickListener(new OnClickListener() {
            boolean reset = false;

            @Override
            public void onClick(View v) {
                imageView.animate()
                        .translationX(reset ? 0 : imageView.getWidth())
                        .rotationY(reset ? 0 : 180)
                        .alpha(reset ? 1 : 0.2f);
                reset = !reset;
            }
        });
        addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}