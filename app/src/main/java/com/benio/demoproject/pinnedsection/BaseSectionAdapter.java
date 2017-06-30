package com.benio.demoproject.pinnedsection;

import java.util.ArrayList;
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
        // position of section is -1, just return the key.
        if (position == -1) {
            return getKey(section);
        }
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

    public Object getKey(int section) {
        return mKeyList.get(section);
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

    public int getSection(Object key) {
        return mKeyList.indexOf(key);
    }

    @Override
    public Object[] getSections() {
        return mKeyList.toArray();
    }

    public boolean addAll(List<? extends T> data, KeyCreator<T> keyCreator) {
        if (keyCreator == null
                || data == null || data.isEmpty()) {
            return false;
        }

        List<T> sectionList = null;
        Object sectionKey = null;
        for (int i = 0, count = data.size(); i < count; i++) {
            T item = data.get(i);
            if (sectionList == null) {
                sectionList = new ArrayList<>();
            }
            sectionList.add(item);

            // 判断前一项与后一项sectionKey是否相同，不同则分组
            sectionKey = keyCreator.getKey(item);
            if (i + 1 >= count) { // 最后一项，将剩余的添加
                add(sectionKey, sectionList);
            } else if (!sectionKey.equals(keyCreator.getKey(data.get(i + 1)))) {
                add(sectionKey, sectionList);
                // 另开分组
                sectionList = null;
            }
        }
        return true;
    }

    public interface KeyCreator<T> {
        Object getKey(T item);
    }
}
