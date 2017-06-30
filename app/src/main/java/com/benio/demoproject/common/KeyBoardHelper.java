package com.benio.demoproject.common;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by zhangzhibin on 2016/11/9.
 */
public class KeyBoardHelper {

    public interface OnKeyboardStateChangedListener {
        void onKeyboardStateChanged(boolean isKeyboardShowing, int heightDiff);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        // 软键盘的显示状态
        private boolean mKeyboardShowing;
        // 状态栏的高度
        private int mStatusBarHeight = -1;

        private Rect mRect = new Rect();

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，
            // 以及ActionBar和状态栏，但不含设备底部的虚拟按键。
            mDecorView.getWindowVisibleDisplayFrame(mRect);
            // 屏幕高度。这个高度不含虚拟按键的高度
            int screenHeight = mDecorView.getRootView().getHeight();
            int heightDiff = screenHeight - mRect.height();

            if (mStatusBarHeight < 0) {
                mStatusBarHeight = getStatusBarHeight(mDecorView.getResources());
            }
            // 在不显示软键盘时，heightDiff等于状态栏的高度
            // 在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
            // 所以heightDiff大于状态栏高度时表示软键盘出现了，
            // 这时可算出软键盘的高度，即heightDiff减去状态栏的高度
            boolean keyboardShowing = heightDiff > mStatusBarHeight;

            //如果之前软键盘状态为显示，现在为关闭，或者之前为关闭，现在为显示，则表示软键盘的状态发生了改变
            if ((mKeyboardShowing && !keyboardShowing) || (!mKeyboardShowing && keyboardShowing)) {
                mKeyboardShowing = keyboardShowing;
                if (mOnKeyboardStateChangedListener != null) {
                    mOnKeyboardStateChangedListener.onKeyboardStateChanged(keyboardShowing, heightDiff);
                }
            }
        }
    };

    private OnKeyboardStateChangedListener mOnKeyboardStateChangedListener;
    private View mDecorView;

    public KeyBoardHelper(Activity activity, OnKeyboardStateChangedListener onKeyboardStateChangedListener) {
        mDecorView = activity.getWindow().getDecorView();
        mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        mOnKeyboardStateChangedListener = onKeyboardStateChangedListener;
    }

    public void destroy() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mDecorView.getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
        } else {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
