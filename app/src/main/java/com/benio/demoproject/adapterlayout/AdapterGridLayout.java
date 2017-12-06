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
    public static final int IGNORE_ITEM_VIEW_TYPE = android.widget.AdapterView.ITEM_VIEW_TYPE_IGNORE;
    private ListAdapter mAdapter;
    private DataSetObserver mObserver;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Drawable mDivider;
    private RecycleBin mRecycleBin = new RecycleBin();

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

    private OnLongClickListener mChildLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            final int pos = indexOfChild(v);
            if ((mOnItemLongClickListener != null) && (mAdapter != null)) {
                return mOnItemLongClickListener.onItemLongClick(AdapterGridLayout.this, v,
                        pos, mAdapter.getItemId(pos));
            }
            return false;
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

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        if (!isLongClickable()) {
            setLongClickable(true);
        }
        mOnItemLongClickListener = listener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(ListAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        setAdapterInternal(adapter);
        reloadChildViews();
    }

    private void setAdapterInternal(ListAdapter adapter) {
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }

        if (getChildCount() > 0) {
            removeAllViews();
        }
        mRecycleBin.clear();

        this.mAdapter = adapter;

        if (adapter != null) {
            if (mObserver == null) {
                mObserver = new AdapterDataSetObserver();
            }
            adapter.registerDataSetObserver(mObserver);

            mRecycleBin.setViewTypeCount(adapter.getViewTypeCount());
        }
    }

    private void reloadChildViews() {
        // Flush any cached views that did not get reused above
        mRecycleBin.scrapActiveViews();

        // remove and recycle views.
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final int viewType = mAdapter.getItemViewType(i);
            final View child = getChildAt(i);
            child.setOnClickListener(null);
            child.setOnLongClickListener(null);
            if (viewType != IGNORE_ITEM_VIEW_TYPE) {
                mRecycleBin.addScrapView(child, i, viewType);
            }
        }

        if (childCount > 0) {
            removeAllViews();
        }

        if (mAdapter == null) {
            return;
        }

        // obtain and add views.
        final boolean areAllItemsEnabled = mAdapter.areAllItemsEnabled();
        final int count = mAdapter.getCount();
        for (int i = 0; i < count; ++i) {
            final View child = obtainView(i);

            if (child != null) {
                final ViewGroup.LayoutParams vlp = child.getLayoutParams();
                final LayoutParams lp;
                if (vlp == null) {
                    lp = generateDefaultLayoutParams();
                } else if (!checkLayoutParams(vlp)) {
                    lp = generateLayoutParams(vlp);
                } else {
                    lp = (LayoutParams) vlp;
                }
                addView(child, -1, lp);
                if (areAllItemsEnabled || mAdapter.isEnabled(i)) {
                    child.setOnClickListener(mChildClickListener);
                    child.setOnLongClickListener(mChildLongClickListener);
                }
            }
        }
    }

    private View obtainView(int position) {
        final int viewType = mAdapter.getItemViewType(position);
        final View scrapView;
        if (viewType != IGNORE_ITEM_VIEW_TYPE) {
            scrapView = mRecycleBin.getScrapView(position, viewType);
        } else {
            scrapView = null;
        }
        final View child = mAdapter.getView(position, scrapView, this);
        if (scrapView != null && child != scrapView) {
            // Failed to re-bind the data, return scrap to the heap.
            mRecycleBin.addScrapView(scrapView, position, viewType);
        }
        return child;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAdapter != null && mObserver == null) {
            mObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mObserver);

            // Data may have changed while we were detached. Refresh.
            mObserver.onChanged();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // Detach any view left in the scrap heap
        mRecycleBin.clear();

        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
            mObserver = null;
        }
    }

    public void setDividerDrawable(Drawable divider) {
        if (divider == mDivider) {
            return;
        }
        mDivider = divider;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
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
