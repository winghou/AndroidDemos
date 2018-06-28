package com.benio.demoproject.compoundbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.benio.demoproject.R;

/**
 * CompoundButton组，用于多选组合，可设置最小和最大选中个数<p>
 * 若checkedCount>=maxCheckedCount，则已选中的CompoundButton可反选，未选中的不可选<br>
 * 若checkedCount<=minCheckedCount，则已选中的CompoundButton不可反选，未选中的可选<br>
 * Created by zhangzhibin on 2017/7/27.
 */
public class CompoundButtonGroup extends LinearLayout {
    private int mCheckedCount;
    private int mMinCheckedCount = 0;
    private int mMaxCheckedCount = Integer.MAX_VALUE;
    private SparseArray<CompoundButton> mButtons = new SparseArray<>();

    // tracks children compound buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public CompoundButtonGroup(Context context) {
        this(context, null);
    }

    public CompoundButtonGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompoundButtonGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CompoundButtonGroup);
        if (typedArray.hasValue(R.styleable.CompoundButtonGroup_cbg_minCheckedCount)) {
            setMinCheckedCount(typedArray.getInt(R.styleable.CompoundButtonGroup_cbg_minCheckedCount, mMinCheckedCount));
        }
        if (typedArray.hasValue(R.styleable.CompoundButtonGroup_cbg_maxCheckedCount)) {
            setMaxCheckedCount(typedArray.getInt(R.styleable.CompoundButtonGroup_cbg_maxCheckedCount, mMaxCheckedCount));
        }
        typedArray.recycle();

        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refreshButtonState();
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

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
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

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CompoundButtonGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CompoundButtonGroup.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return CompoundButtonGroup.class.getName();
    }

    /**
     * <p>This set of layout parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class used the value read from the XML file.</p>
     * <p>
     * <p>See
     * {@link android.R.styleable#LinearLayout_Layout LinearLayout Attributes}
     * for a list of all child view attributes that this class supports.</p>
     */
    public static class LayoutParams extends LinearLayout.LayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        /**
         * <p>Fixes the child's width to
         * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and the child's
         * height to  {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
         * when not specified in the XML file.</p>
         *
         * @param a          the styled attributes set
         * @param widthAttr  the width attribute to fetch
         * @param heightAttr the height attribute to fetch
         */
        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * checkbox changed in this group.</p>
     */
    public interface OnCheckedChangeListener {

        public void onCheckedChanged(CompoundButtonGroup group, @IdRes int checkedId, boolean isChecked);
    }

    /**
     * 根据当前选中的CompoundButton个数，刷新CompoundButton的状态<p></p>
     * 若checkedCount>=maxCheckedCount，则已选中的CompoundButton可反选，未选中的不可选</br>
     * 若checkedCount<=minCheckedCount，则已选中的CompoundButton不可反选，未选中的可选</br>
     * 若minCheckedCount<checkedCount<maxCheckedCount，则无限制
     */
    private void refreshButtonState() {
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
                mOnCheckedChangeListener.onCheckedChanged(CompoundButtonGroup.this,
                        buttonView.getId(), isChecked);
            }
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        public void onChildViewAdded(View parent, View child) {
            if (parent == CompoundButtonGroup.this && child instanceof CompoundButton) {
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
            if (parent == CompoundButtonGroup.this && child instanceof CompoundButton) {
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
    }

}