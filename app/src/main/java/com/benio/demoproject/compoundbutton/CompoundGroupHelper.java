package com.benio.demoproject.compoundbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.benio.demoproject.R;

/**
 * 管理CompoundButton组，用于多选组合，可设置最小和最大选中个数<p>
 * 若checkedCount>=maxCheckedCount，则已选中的CompoundButton可反选，未选中的不可选<br>
 * 若checkedCount<=minCheckedCount，则已选中的CompoundButton不可反选，未选中的可选<br>
 * Created by zhangzhibin on 2018/6/27.
 */
public class CompoundGroupHelper implements ViewGroup.OnHierarchyChangeListener {
    private final ViewGroup mParent;

    private int mCheckedCount;
    private int mMinCheckedCount = 0;
    private int mMaxCheckedCount = Integer.MAX_VALUE;
    private SparseArray<CompoundButton> mButtons = new SparseArray<>();

    // tracks children compound buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CompoundGroupHelper(@NonNull ViewGroup parent) {
        mParent = parent;
        mChildOnCheckedChangeListener = new CheckedStateTracker();
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        final Context context = mParent.getContext();
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CompoundGroup, defStyleAttr, 0);
        if (typedArray.hasValue(R.styleable.CompoundGroup_cg_minCheckedCount)) {
            setMinCheckedCount(typedArray.getInt(R.styleable.CompoundGroup_cg_minCheckedCount, mMinCheckedCount));
        }
        if (typedArray.hasValue(R.styleable.CompoundGroup_cg_maxCheckedCount)) {
            setMaxCheckedCount(typedArray.getInt(R.styleable.CompoundGroup_cg_maxCheckedCount, mMaxCheckedCount));
        }
        typedArray.recycle();
    }

    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mOnHierarchyChangeListener = listener;
    }

    /**
     * 设置最小选中个数
     *
     * @param minCheckedCount 0 <= {@code minCheckedCount} <= {@code maxCheckedCount}
     */
    public void setMinCheckedCount(int minCheckedCount) {
        if (minCheckedCount < 0) {
            minCheckedCount = 0;
        } else if (minCheckedCount >= mMaxCheckedCount) {
            throw new IllegalArgumentException("MinCheckedCount cannot be greater than maxCheckedCount.");
        }
        mMinCheckedCount = minCheckedCount;
    }

    public int getMinCheckedCount() {
        return mMinCheckedCount;
    }

    /**
     * 设置最大选中个数
     *
     * @param maxCheckedCount {@code maxCheckedCount} > {@code minCheckedCount} >= 0
     */
    public void setMaxCheckedCount(int maxCheckedCount) {
        if (maxCheckedCount <= 0) {
            maxCheckedCount = Integer.MAX_VALUE;
        } else if (maxCheckedCount <= mMinCheckedCount) {
            throw new IllegalArgumentException("MaxCheckedCount cannot be less than minCheckedCount.");
        }
        mMaxCheckedCount = maxCheckedCount;
    }

    public int getMaxCheckedCount() {
        return mMaxCheckedCount;
    }

    /**
     * Returns the set of checked button ids.
     *
     * @return A new array which contains the id of each checked button.
     */
    public int[] getCheckedButtonIds() {
        final SparseArray<CompoundButton> buttons = mButtons;
        final int checkedCount = mCheckedCount;
        final int[] ids = new int[checkedCount];
        CompoundButton button;
        int j = 0, size = buttons.size();
        for (int i = 0; i < size && j <= checkedCount; i++) {
            button = buttons.valueAt(i);
            if (button.isChecked()) {
                ids[j++] = button.getId();
            }
        }
        return ids;
    }

    public int getCheckedCount() {
        return mCheckedCount;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * 根据当前选中的CompoundButton个数，刷新CompoundButton的状态<p></p>
     * 若checkedCount>=maxCheckedCount，则已选中的ToggleButton可反选，未选中的不可选</br>
     * 若checkedCount<=minCheckedCount，则已选中的ToggleButton不可反选，未选中的可选</br>
     * 若minCheckedCount<checkedCount<maxCheckedCount，则无限制
     */
    public void refreshButtonState() {
        final int checkedCount = mCheckedCount;
        if (checkedCount > mMaxCheckedCount || checkedCount < mMinCheckedCount) {
            throw new IllegalArgumentException("CheckedCount: " + mCheckedCount +
                    ", MaxCheckedCount: " + mMaxCheckedCount + ", MinCheckedCount: " + mMinCheckedCount);
        }

        final SparseArray<CompoundButton> buttons = mButtons;
        boolean checkedViewState, uncheckedViewState;
        if (checkedCount >= mMaxCheckedCount) {
            checkedViewState = true;
            uncheckedViewState = false;
        } else if (checkedCount <= mMinCheckedCount) {
            checkedViewState = false;
            uncheckedViewState = true;
        } else {
            checkedViewState = true;
            uncheckedViewState = true;
        }
        CompoundButton button;
        for (int i = 0, size = buttons.size(); i < size; i++) {
            button = buttons.valueAt(i);
            button.setClickable(button.isChecked() ?
                    checkedViewState : uncheckedViewState);
        }
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mCheckedCount++;
            } else {
                mCheckedCount--;
            }
            refreshButtonState();

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(mParent, buttonView.getId(), isChecked);
            }
        }
    }

    public void onChildViewAdded(View parent, View child) {
        if (parent == mParent && acceptChild(child)) {
            final CompoundButton button = (CompoundButton) child;
            button.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
            if (button.isChecked()) {
                mCheckedCount++;
            }
            // generates an id if it's missing
            int id = child.getId();
            if (id == View.NO_ID) {
                id = child.hashCode();
                child.setId(id);
            }
            mButtons.put(id, button);
        }

        if (mOnHierarchyChangeListener != null) {
            mOnHierarchyChangeListener.onChildViewAdded(parent, child);
        }
    }

    public void onChildViewRemoved(View parent, View child) {
        if (parent == mParent && acceptChild(child)) {
            final CompoundButton button = (CompoundButton) child;
            button.setOnCheckedChangeListener(null);
            if (button.isChecked()) {
                mCheckedCount--;
            }
            mButtons.remove(child.getId());
        }

        if (mOnHierarchyChangeListener != null) {
            mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
        }
    }

    /**
     * 判断是否接受CompoundGroupHelper的管理
     *
     * @param child
     * @return
     */
    public boolean acceptChild(View child) {
        return child instanceof CompoundButton;
    }
}
