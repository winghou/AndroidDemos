package com.benio.demoproject.common;

import android.support.annotation.StringRes;

/**
 * Created by zhangzhibin on 2018/6/21.
 */
public interface LoadingView {
    /**
     * 显示Loading，无文字
     */
    void showLoading();

    /**
     * 显示Loading
     *
     * @param msg Loading的提示语
     */
    void showLoading(CharSequence msg);

    /**
     * 显示Loading
     *
     * @param resId Loading的提示语
     */
    void showLoading(@StringRes int resId);

    void hideLoading();
}
