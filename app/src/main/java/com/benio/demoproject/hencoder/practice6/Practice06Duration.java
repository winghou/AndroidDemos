package com.benio.demoproject.hencoder.practice6;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.benio.demoproject.R;

public class Practice06Duration extends LinearLayout {
    Button animateBt;
    ImageView imageView;
    int duration = 300;

    public Practice06Duration(Context context) {
        super(context);
        init(context);
    }

    public Practice06Duration(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Practice06Duration(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                        .alpha(reset ? 1 : 0.2f)
                        .setDuration(duration);
                reset = !reset;
            }
        });
        SeekBar seekBar = new SeekBar(context);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = progress * 50;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        addView(imageView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(seekBar);
        addView(animateBt, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}