package com.benio.demoproject.adapterlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListAdapter;

/**
 * LinearLayout for {@link android.widget.BaseAdapter}
 * Created by zhangzhibin on 2016/10/19.
 */
public class AdapterLinearLayout extends LinearLayoutCompat implements AdapterView<ListAdapter> {
    public static final int IGNORE_ITEM_VIEW_TYPE = android.widget.AdapterView.ITEM_VIEW_TYPE_IGNORE;
    private ListAdapter mAdapter;
    private DataSetObserver mObserver;
    private OnItemClickListener mOnItemClickListener;
    private RecycleBin mRecycleBin;

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
                mOnItemClickListener.onItemClick(AdapterLinearLayout.this, v,
                        pos, mAdapter.getItemId(pos));
            }
        }
    };

    public AdapterLinearLayout(Context context) {
        super(context);
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

            if (mRecycleBin == null) {
                mRecycleBin = new RecycleBin();
            }
            mRecycleBin.setViewTypeCount(adapter.getViewTypeCount());
        }
    }

    private void reloadChildViews() {
        mRecycleBin.scrapActiveViews();

        // remove and recycle views.
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final int viewType = mAdapter.getItemViewType(i);
            final View child = getChildAt(i);
            child.setOnClickListener(null);
            if (viewType != IGNORE_ITEM_VIEW_TYPE) {
                mRecycleBin.addScrapView(child, i, viewType);
            }
        }
        removeViewsInLayout(0, childCount);

        if (mAdapter == null) {
            return;
        }

        // obtain and add views.
        final boolean areAllItemsEnabled = mAdapter.areAllItemsEnabled();
        final int count = mAdapter.getCount();
        for (int i = 0; i < count; ++i) {
            final int viewType = mAdapter.getItemViewType(i);
            final View scrapView;
            if (viewType != IGNORE_ITEM_VIEW_TYPE) {
                scrapView = mRecycleBin.getScrapView(i, viewType);
            } else {
                scrapView = null;
            }
            final View child = mAdapter.getView(i, scrapView, this);
            if (child != null) {
                ViewGroup.LayoutParams params = child.getLayoutParams();
                if (params == null) {
                    params = generateDefaultLayoutParams();
                }
                addViewInLayout(child, -1, params, true);
                if (areAllItemsEnabled || mAdapter.isEnabled(i)) {
                    child.setOnClickListener(mChildClickListener);
                }
            }
        }

        requestLayout();
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
}