package com.benio.demoproject.common.listview;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;

import com.benio.demoproject.common.ViewFinder;

/**
 * ViewFinder 实现类
 * Created by zhangzhibin on 2016/1/15.
 */
public class ViewCache extends ViewHolder implements ViewFinder {
    private SparseArrayCompat<View> mViews;

    public ViewCache(@NonNull View itemView) {
        super(itemView);
        this.mViews = new SparseArrayCompat<>();
    }

    public ViewCache(@NonNull View itemView, int capacity) {
        super(itemView);
        this.mViews = new SparseArrayCompat<>(capacity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (null == view) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }
}
