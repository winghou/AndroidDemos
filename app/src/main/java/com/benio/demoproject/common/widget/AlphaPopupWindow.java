package com.benio.demoproject.common.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

import com.benio.demoproject.common.utils.ActivityUtils;


/**
 * AlphaPopupWindow：显示时窗口变暗，消失恢复
 * Created by zhangzhibin on 2016/6/8.
 */
public class AlphaPopupWindow extends PopupWindow {
    /**
     * popupWindow显示时窗口默认alpha值
     */
    private static final float DEFAULT_ALPHA = 0.5F;
    /**
     * popupWindow默认窗口默认alpha值
     */
    private static final float DISMISS_ALPHA = 1F;
    /**
     * 显示popupWindow时窗口的alpha值
     */
    private float mAlpha = DEFAULT_ALPHA;
    /**
     * 显示popupWindow时是否改变窗口alpha值
     */
    private boolean mChangeAlpha = true;

    public AlphaPopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public AlphaPopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public AlphaPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        init(contentView, focusable);
    }

    private void init(View contentView, boolean focusable) {
        if (focusable) {
            //点击空白处dismiss
            setFocusable(true);
            setOutsideTouchable(true);
            setBackgroundDrawable(new BitmapDrawable());
        }

        initView(contentView);
    }

    /**
     * 初始化view
     *
     * @param contentView
     */
    protected void initView(View contentView) {

    }

    public boolean isChangeAlpha() {
        return mChangeAlpha;
    }

    /**
     * 设置显示popupWindow时是否改变窗口alpha值
     */
    public void setChangeAlpha(boolean changeable) {
        this.mChangeAlpha = changeable;
    }

    /**
     * 设置popupWindow显示时窗口alpha值
     */
    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    /**
     * @return popupWindow显示时窗口alpha值
     */
    public float getAlpha() {
        return mAlpha;
    }

    public Context getContext() {
        View contentView = getContentView();
        return null == contentView ? null : contentView.getContext();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        performChangeWindowAlpha(DISMISS_ALPHA);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        performChangeWindowAlpha(mAlpha);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        performChangeWindowAlpha(mAlpha);
    }

    /**
     * 改变窗口alpha值
     *
     * @param alpha
     */
    private void performChangeWindowAlpha(float alpha) {
        if (!mChangeAlpha) {
            return;
        }

        View contentView = getContentView();
        if (contentView == null) {
            return;
        }

        final Activity activity = ActivityUtils.getActivity(contentView);
        ActivityUtils.setWindowAlpha(activity, alpha);
    }
}