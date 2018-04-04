package com.benio.demoproject.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ListView;

/**
 * This class provides sticky header functionality in a list view, to use with
 * SetupWizardIllustration. To use this, add a header tagged with "sticky", or a header tagged with
 * "stickyContainer" and one of its child tagged as "sticky". The sticky container will be drawn
 * when the sticky element hits the top of the view.
 *
 * There are a few things to note:
 * 1. The two supported scenarios are StickyHeaderListView -> Header (stickyContainer) -> sticky,
 *    and StickyHeaderListView -> Header (sticky). The arrow (->) represents parent/child
 *    relationship and must be immediate child.
 * 2. The view does not work well with padding. b/16190933
 * 3. If fitsSystemWindows is true, then this will offset the sticking position by the height of
 *    the system decorations at the top of the screen.
 *
 * @see SetupWizardIllustration
 * @see com.google.android.setupwizard.util.StickyHeaderScrollView
 *
 * Copied from com.google.android.setupwizard.util.StickyHeaderListView
 */
public class StickyHeaderListView extends ListView {

    private View mSticky;
    private View mStickyContainer;
    private int mStatusBarInset = 0;
    private RectF mStickyRect = new RectF();

    public StickyHeaderListView(Context context) {
        super(context);
        init(null, android.R.attr.listViewStyle);
    }

    public StickyHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, android.R.attr.listViewStyle);
    }

    public StickyHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
//        final TypedArray a = getContext().obtainStyledAttributes(attrs,
//                R.styleable.SuwStickyHeaderListView, defStyleAttr, 0);
//        int headerResId = a.getResourceId(R.styleable.SuwStickyHeaderListView_suwHeader, 0);
//        if (headerResId != 0) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            View header = inflater.inflate(headerResId, this, false);
//            addHeaderView(header);
//        }
//        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mSticky == null) {
            updateStickyView();
        }
    }

    public void updateStickyView() {
        mSticky = findViewWithTag("sticky");
        mStickyContainer = findViewWithTag("stickyContainer");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mStickyRect.contains(ev.getX(), ev.getY())) {
            ev.offsetLocation(-mStickyRect.left, -mStickyRect.top);
            return mStickyContainer.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mSticky != null) {
            final int saveCount = canvas.save();
            // The view to draw when sticking to the top
            final View drawTarget = mStickyContainer != null ? mStickyContainer : mSticky;
            // The offset to draw the view at when sticky
            final int drawOffset = mStickyContainer != null ? mSticky.getTop() : 0;
            // Position of the draw target, relative to the outside of the scrollView
            final int drawTop = drawTarget.getTop();
            if (drawTop + drawOffset < mStatusBarInset || !drawTarget.isShown()) {
                // ListView does not translate the canvas, so we can simply draw at the top
                mStickyRect.set(0, -drawOffset + mStatusBarInset, drawTarget.getWidth(),
                        drawTarget.getHeight() - drawOffset + mStatusBarInset);
                canvas.translate(0, mStickyRect.top);
                canvas.clipRect(0, 0, drawTarget.getWidth(), drawTarget.getHeight());
                drawTarget.draw(canvas);
            } else {
                mStickyRect.setEmpty();
            }
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (getFitsSystemWindows()) {
            mStatusBarInset = insets.getSystemWindowInsetTop();
            insets.replaceSystemWindowInsets(
                    insets.getSystemWindowInsetLeft(),
                    0, /* top */
                    insets.getSystemWindowInsetRight(),
                    insets.getSystemWindowInsetBottom()
            );
        }
        return insets;
    }
}