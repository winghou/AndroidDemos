package com.benio.demoproject.common.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhangzhibin on 2018/8/17.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public RecyclerViewHolder(View itemView, int size) {
        super(itemView);
        mViews = new SparseArray<>(size);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (null == view) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public TextView getTextView(@IdRes int viewId) {
        return (TextView) getView(viewId);
    }

    public ImageView getImageView(@IdRes int viewId) {
        return (ImageView) getView(viewId);
    }

    public Button getButton(@IdRes int viewId) {
        return (Button) getView(viewId);
    }

    public final Context getContext() {
        return itemView.getContext();
    }
}
