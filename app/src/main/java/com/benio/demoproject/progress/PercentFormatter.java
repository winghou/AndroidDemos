package com.benio.demoproject.progress;

import android.support.annotation.NonNull;

public class PercentFormatter implements Formatter {
    private int oldValue = -1;
    private String oldStr = null;

    @NonNull
    @Override
    public String format(int value) {
        if (oldValue != value) {
            oldValue = value;
            oldStr = value + "%";
        }
        return oldStr;
    }
}