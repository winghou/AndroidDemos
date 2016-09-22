package com.benio.demoproject.progress;

import android.support.annotation.NonNull;

public class PercentFormatter implements Formatter {
    private float oldValue = -1;
    private String oldStr = null;

    @NonNull
    @Override
    public String format(float value) {
        if (oldValue != value) {
            oldValue = value;
            oldStr = value + "%";
        }
        return oldStr;
    }
}