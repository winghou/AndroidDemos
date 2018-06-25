package com.benio.demoproject.common;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 用于倒计时显示
 * Created by zhangzhibin on 2018/6/14.
 */
public class TextViewTimer extends CountDownTimer {
    private static final int INTERVAL = 1000;
    private CharSequence mOriginalText;
    private Reference<TextView> mTextView;
    private String mPattern;

    public TextViewTimer(TextView textView, int seconds) {
        this(textView, seconds, "%ds");
    }

    public TextViewTimer(TextView textView, int seconds, String pattern) {
        super(seconds * INTERVAL, INTERVAL);
        mPattern = pattern;
        mOriginalText = textView.getText();
        mTextView = new WeakReference<>(textView);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int second = (int) (millisUntilFinished / INTERVAL);
        TextView textView = mTextView.get();
        if (textView != null) {
            textView.setEnabled(false);
            textView.setText(String.format(mPattern, second));
        }
    }

    @Override
    public void onFinish() {
        TextView textView = mTextView.get();
        if (textView != null) {
            textView.setEnabled(true);
            textView.setText(mOriginalText);
        }
    }
}