package com.benio.demoproject.compoundbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

/**
 * Created by zhangzhibin on 2018/7/9.
 */
public class ToggleGroup extends LinearLayout {
    private CompoundGroupHelper mGroupHelper;

    public ToggleGroup(Context context) {
        this(context, null);
    }

    public ToggleGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGroupHelper = new CompoundGroupHelper(this) {
            @Override
            public boolean acceptChild(View child) {
                // only accept ToggleButton
                return child instanceof ToggleButton;
            }
        };
        mGroupHelper.loadFromAttributes(attrs, defStyleAttr);
        super.setOnHierarchyChangeListener(mGroupHelper);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mGroupHelper.setOnHierarchyChangeListener(listener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mGroupHelper.refreshButtonState();
    }

    /**
     * 设置最小选中个数
     *
     * @param minCheckedCount 0 <= {@code minCheckedCount} <= {@code maxCheckedCount}
     */
    public void setMinCheckedCount(int minCheckedCount) {
        mGroupHelper.setMinCheckedCount(minCheckedCount);
    }

    public int getMinCheckedCount() {
        return mGroupHelper.getMinCheckedCount();
    }

    /**
     * 设置最大选中个数
     *
     * @param maxCheckedCount {@code maxCheckedCount} > {@code minCheckedCount} >= 0
     */
    public void setMaxCheckedCount(int maxCheckedCount) {
        mGroupHelper.setMaxCheckedCount(maxCheckedCount);
    }

    public int getMaxCheckedCount() {
        return mGroupHelper.getMaxCheckedCount();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mGroupHelper.setOnCheckedChangeListener(listener);
    }

    /**
     * Returns the set of checked button ids.
     *
     * @return A new array which contains the id of each checked button.
     */
    public int[] getCheckedButtonIds() {
        return mGroupHelper.getCheckedButtonIds();
    }

    public int getCheckedCount() {
        return mGroupHelper.getCheckedCount();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ToggleGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ToggleGroup.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return ToggleGroup.class.getName();
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
}
