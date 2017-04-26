package com.benio.demoproject.adapterlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridLayout;

/**
 * Created by benio on 2017/3/12.
 */
public class AdapterGridLayout extends GridLayout implements AdapterView<Adapter> {
    private Adapter mAdapter;
    private DataSetObserver mObserver;

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

    public AdapterGridLayout(Context context) {
        super(context);
    }

    public AdapterGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        setAdapterInternal(adapter);
        reloadChildViews();
    }

    private void setAdapterInternal(Adapter adapter) {
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
}
