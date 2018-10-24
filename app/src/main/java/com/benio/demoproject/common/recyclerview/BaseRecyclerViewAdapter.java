package com.benio.demoproject.common.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benio.demoproject.common.OnItemClickListener;
import com.benio.demoproject.common.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhangzhibin on 2018/8/17.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private List<T> mData = new ArrayList<>();
    private boolean mNotifyOnChange = true;

    public BaseRecyclerViewAdapter() {
    }

    public BaseRecyclerViewAdapter(Collection<? extends T> data) {
        mData.addAll(data);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public VH onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }

        final VH holder = onCreateViewHolder(mInflater, parent, viewType);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(parent, v, holder.getLayoutPosition(),
                            holder.getItemId());
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(parent, v,
                            holder.getLayoutPosition(), holder.getItemId());
                }
            });
        }
        return holder;
    }

    protected abstract VH onCreateViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent,
                                             int viewType);

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public boolean isNotifyOnChange() {
        return mNotifyOnChange;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    protected List<T> getRawData() {
        return mData;
    }

    public List<T> getData() {
        return new ArrayList<>(mData);
    }

    public boolean add(T elem) {
        int lastIndex = mData.size();
        if (mData.add(elem)) {
            if (mNotifyOnChange) notifyItemInserted(lastIndex);
            return true;
        }
        return false;
    }

    public void add(int position, T elem) {
        mData.add(position, elem);
        if (mNotifyOnChange) notifyItemInserted(position);
    }

    public T remove(int position) {
        T item = mData.remove(position);
        if (mNotifyOnChange) notifyItemRemoved(position);
        return item;
    }

    public boolean remove(T elem) {
        int index = mData.indexOf(elem);
        if (mData.remove(elem)) {
            if (mNotifyOnChange) notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    public T set(int position, T elem) {
        T item = mData.set(position, elem);
        if (mNotifyOnChange) notifyItemChanged(position);
        return item;
    }

    public boolean addAll(Collection<? extends T> collection) {
        if (null == collection) {
            return false;
        }
        int lastIndex = mData.size();
        if (mData.addAll(collection)) {
            if (mNotifyOnChange) notifyItemRangeInserted(lastIndex, collection.size());
            return true;
        }
        return false;
    }

    public void setData(List<T> data) {
        mData.clear();
        if (data != null && !data.isEmpty()) {
            mData.addAll(data);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        int size = mData.size();
        if (size > 0) {
            mData.clear();
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }

    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mData, comparator);
        if (mNotifyOnChange) notifyDataSetChanged();
    }
}
