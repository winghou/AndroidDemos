package com.benio.demoproject.common;

public interface MessageView {
    void showMessage(CharSequence msg);

    void showMessage(int resId);
}