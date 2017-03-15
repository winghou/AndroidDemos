package com.benio.demoproject.pinnedsection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhangzhibin on 2017/3/13.
 */
public abstract class BaseSectionAdapter<T> extends PinnedSectionAdapter {
    /**
     * collection for section to data
     */
    private List<List<T>> mData = new LinkedList<>();
    /**
     * collection for section to key
     */
    private List<Object> mKeyList = new LinkedList<>();

    @Override
    public Object getItem(int section, int position) {
        List<T> list = mData.get(section);
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int section, int position) {
        return 0;
    }

    @Override
    public int getSectionCount() {
        return mData.size();
    }

    @Override
    public int getCountForSection(int section) {
        List<T> list = mData.get(section);
        return list != null ? list.size() : 0;
    }

    /**
     * Add a section
     *
     * @param key  key to specified a section
     * @param data
     * @return
     */
    public boolean add(Object key, List<T> data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        int section = mKeyList.indexOf(key);
        if (section < 0) {
            // new key，则添加到新的section中
            mKeyList.add(key);
            mData.add(data);
        } else {
            // old key，添加到原有的section中
            List<T> oldData = mData.get(section);
            if (oldData != null) {
                oldData.addAll(data);
            } else {
                mData.set(section, data);
            }
        }
        return true;
    }

    /**
     * remove a section which specified by key
     *
     * @param key key to specified the section that you want to remove.
     * @return
     */
    public boolean remove(Object key) {
        int section = mKeyList.indexOf(key);
        if (section < 0) {
            return false;
        }
        mData.remove(section);
        mKeyList.remove(section);
        return true;
    }

    public void clear() {
        mData.clear();
        mKeyList.clear();
    }
}
