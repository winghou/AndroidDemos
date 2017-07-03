package com.benio.demoproject.adapterlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListAdapter;

import com.benio.demoproject.R;

/**
 * Created by benio on 2017/3/12.
 */
public class AdapterGridLayout extends GridLayout implements AdapterView<ListAdapter> {
    private ListAdapter mAdapter;
    private DataSetObserver mObserver;
    private OnItemClickListener mOnItemClickListener;
    private Drawable mDivider;

    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            reloadChildViews();
        }

        @Override
        public void onInvalidated() {
            reloadChildViews();
        }
    }

    private OnClickListener mChildClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final int pos = indexOfChild(v);
            if ((mOnItemClickListener != null) && (mAdapter != null)) {
                mOnItemClickListener.onItemClick(AdapterGridLayout.this, v,
                        pos, mAdapter.getItemId(pos));
            }
        }
    };

    public AdapterGridLayout(Context context) {
        super(context);
        init(context, null);
    }

    public AdapterGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AdapterGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdapterGridLayout);
        try {
            setDividerDrawable(a.getDrawable(R.styleable.AdapterGridLayout_divider));
        } finally {
            a.recycle();
        }
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Nullable
    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public boolean performItemClick(View view, int position, long id) {
        final boolean result;
        if (mOnItemClickListener != null) {
            playSoundEffect(SoundEffectConstants.CLICK);
            mOnItemClickListener.onItemClick(this, view, position, id);
            result = true;
        } else {
            result = false;
        }

        if (view != null) {
            view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
        }
        return result;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ListAdapter adapter) {
        setAdapterInternal(adapter);
        reloadChildViews();
    }

    private void setAdapterInternal(ListAdapter adapter) {
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        this.mAdapter = adapter;
        if (adapter != null) {
            if (mObserver == null) {
                mObserver = new AdapterDataSetObserver();
            }
            adapter.registerDataSetObserver(mObserver);
        }
    }

    private void reloadChildViews() {
        removeAllViews();

        if (mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

        int count = mAdapter.getCount();
        for (int i = 0; i < count; ++i) {
            // Nothing found in the recycler -- ask the adapter for a view
            View child = mAdapter.getView(i, null, this);
            if (child != null) {
                ViewGroup.LayoutParams params = child.getLayoutParams();
                if (params == null) {
                    params = generateDefaultLayoutParams();
                }
                addViewInLayout(child, -1, params, true);
                if (mAdapter.areAllItemsEnabled() || mAdapter.isEnabled(i)) {
                    child.setOnClickListener(mChildClickListener);
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAdapter != null && mObserver == null) {
            mObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mObserver = null;
    }


    public void setDividerDrawable(Drawable divider) {
        if (divider == mDivider) {
            return;
        }
        mDivider = divider;
        setWillNotDraw(divider == null);
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDivider == null) {
            return;
        }
        drawDividersVertical(canvas);
        drawDividersHorizontal(canvas);
    }

    private static int resolveLayoutMargin(int margin) {
        return margin == UNDEFINED ? 0 : margin;
    }

    private void drawDividersVertical(Canvas canvas) {
        final int count = getChildCount();
        final int columnCount = getColumnCount();
        final int rowCount = getRowCount();
        for (int i = 0; i < count; i++) {
            // 如果是最后一行，则不需要绘制底部分隔符
            if (i / columnCount >= rowCount - 1) {
                break;
            }
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int left = child.getLeft() - resolveLayoutMargin(lp.leftMargin);
                final int right = child.getRight() + resolveLayoutMargin(lp.rightMargin);
                final int top = child.getBottom() + resolveLayoutMargin(lp.bottomMargin);
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
    }

    private void drawDividersHorizontal(Canvas canvas) {
        final int count = getChildCount();
        final int columnCount = getColumnCount();
        for (int i = 0; i < count; i++) {
            // 如果是最后一列，则不需要绘制右边
            if ((i + 1) % columnCount == 0) {
                continue;
            }
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + resolveLayoutMargin(lp.rightMargin);
                final int right = left + mDivider.getIntrinsicWidth();
                final int top = child.getTop() + resolveLayoutMargin(lp.topMargin);
                final int bottom = child.getBottom() + resolveLayoutMargin(lp.bottomMargin);
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
    }
}
