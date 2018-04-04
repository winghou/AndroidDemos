package com.benio.demoproject.common.widget;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * An extension of ScrollView that will invoke a listener callback when the ScrollView needs
 * scrolling, and when the ScrollView is being scrolled to the bottom. This is often used in Setup
 * Wizard as a way to ensure that users see all the content before proceeding.
 */
public class BottomScrollView extends ScrollView {

    public interface BottomScrollListener {
        void onScrolledToBottom();
        void onRequiresScroll();
    }

    private BottomScrollListener mListener;
    private int mScrollThreshold;
    private boolean mRequiringScroll = false;

    private final Runnable mCheckScrollRunnable = new Runnable() {
        @Override
        public void run() {
            checkScroll();
        }
    };

    public BottomScrollView(Context context) {
        super(context);
    }

    public BottomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBottomScrollListener(BottomScrollListener l) {
        mListener = l;
    }

    @VisibleForTesting
    public int getScrollThreshold() {
        return mScrollThreshold;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final View child = getChildAt(0);
        if (child != null) {
            mScrollThreshold = Math.max(0, child.getMeasuredHeight() - b + t - getPaddingBottom());
        }
        if (b - t > 0) {
            // Post check scroll in the next run loop, so that the callback methods will be invoked
            // after the layout pass. This way a new layout pass will be scheduled if view
            // properties are changed in the callbacks.
            post(mCheckScrollRunnable);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (oldt != t) {
            checkScroll();
        }
    }

    private void checkScroll() {
        if (mListener != null) {
            if (getScrollY() >= mScrollThreshold) {
                mListener.onScrolledToBottom();
            } else if (!mRequiringScroll) {
                mRequiringScroll = true;
                mListener.onRequiresScroll();
            }
        }
    }

}