package com.benio.demoproject.hencoder.practice6;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.benio.demoproject.R;

/**
 * Interpolator 就别练了，没什么好练的，Practice 和 Sample 的代码是一毛一样的。
 * 它的关键是理解，所以还是去看几眼实际效果吧。
 */
public class Practice07Interpolator extends LinearLayout {
    Spinner spinner;
    Button animateBt;
    ImageView imageView;

    Interpolator[] interpolators = new Interpolator[13];
    Path interpolatorPath;

    public Practice07Interpolator(Context context) {
        super(context);
        init(context);
    }

    public Practice07Interpolator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice07Interpolator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        interpolatorPath = new Path();
        interpolatorPath.lineTo(0.25f, 0.25f);
        interpolatorPath.moveTo(0.25f, 1.5f);
        interpolatorPath.lineTo(1, 1);
        interpolators[0] = new AccelerateDecelerateInterpolator();
        interpolators[1] = new LinearInterpolator();
        interpolators[2] = new AccelerateInterpolator();
        interpolators[3] = new DecelerateInterpolator();
        interpolators[4] = new AnticipateInterpolator();
        interpolators[5] = new OvershootInterpolator();
        interpolators[6] = new AnticipateOvershootInterpolator();
        interpolators[7] = new BounceInterpolator();
        interpolators[8] = new CycleInterpolator(0.5f);
        interpolators[9] = PathInterpolatorCompat.create(interpolatorPath);
        interpolators[10] = new FastOutLinearInInterpolator();
        interpolators[11] = new FastOutSlowInInterpolator();
        interpolators[12] = new LinearOutSlowInInterpolator();

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
                        .translationX(reset ? 0 : getWidth() - imageView.getWidth())
                        .setInterpolator(interpolators[spinner.getSelectedItemPosition()]);
                reset = !reset;
            }
        });
        spinner = new Spinner(context);
        final CharSequence[] entries = context.getResources().getTextArray(R.array.interpolators);
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                context, android.R.layout.simple_spinner_item, entries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(spinner, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}