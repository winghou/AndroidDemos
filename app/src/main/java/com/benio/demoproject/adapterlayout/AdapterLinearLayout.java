package com.benio.demoproject.adapterlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * LinearLayout for {@link android.widget.BaseAdapter}
 * Created by zhangzhibin on 2016/10/19.
 */
public class AdapterLinearLayout extends LinearLayoutCompat {

    private BaseAdapter mAdapter;
    private DataSetObserver mObserver;

    public AdapterLinearLayout(Context context) {
        super(context);
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        setAdapterInternal(adapter);
        reloadChildViews();
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

    private void setAdapterInternal(BaseAdapter adapter) {
        unRegisterObserver();
        this.mAdapter = adapter;
        if (adapter != null) {
            if (mObserver == null) {
                mObserver = new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        reloadChildViews();
                    }

                    @Override
                    public void onInvalidated() {
                        reloadChildViews();
                    }
                };
            }
            adapter.registerDataSetObserver(mObserver);
        }
    }

    private void unRegisterObserver() {
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterObserver();
    }
}