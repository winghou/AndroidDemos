package com.benio.demoproject.compoundbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.benio.demoproject.R;
import com.benio.demoproject.common.utils.ViewUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhangzhibin on 2017/7/27.
 */
public class CompoundButtonGroup extends LinearLayout {
    private static final String TAG = "xxxx";

    @IntDef({CHECK_MODE_SINGLE, CHECK_MODE_MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CheckMode {
    }

    public static final int CHECK_MODE_SINGLE = 0;
    public static final int CHECK_MODE_MULTIPLE = 1;
    private int mCheckMode = CHECK_MODE_SINGLE;

    private int mMinCheckedCount;
    private int mCheckedItemCount;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray(0);

    // when true, mChildCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private CompoundButton.OnCheckedChangeListener mChildCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "onCheckedChanged() called isChecked = [" + isChecked + "], mProtectFromCheckedChange = " + mProtectFromCheckedChange);
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            toggleInGroup(buttonView);
        }
    };

    public CompoundButtonGroup(Context context) {
        super(context);
        init(context, null);
    }

    public CompoundButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CompoundButtonGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CompoundButtonGroup);
        mCheckMode = typedArray.getInt(R.styleable.CompoundButtonGroup_cbg_checkMode, mCheckMode);
        setMinCheckedCount(typedArray.getInt(R.styleable.CompoundButtonGroup_cbg_minCheckedCount, mMinCheckedCount));
        typedArray.recycle();
    }

    public void setCheckMode(@CheckMode int checkMode) {
        if (mCheckMode == checkMode) {
            return;
        }
        mCheckMode = checkMode;
        // clear previous state
        clearCheck();
    }

    @CheckMode
    public int getCheckMode() {
        return mCheckMode;
    }

    /**
     * 设置最少选中个数，多选情况下才生效
     *
     * @param minCheckedCount
     */
    public void setMinCheckedCount(int minCheckedCount) {
        if (minCheckedCount < 0) {
            minCheckedCount = 0;
        }
        mMinCheckedCount = minCheckedCount;
    }

    public int getMinCheckedCount() {
        return mMinCheckedCount;
    }

    public void clearCheck() {
        mCheckStates.clear();
        mCheckedItemCount = 0;
        updateCompoundButtons();
    }

    public SparseBooleanArray getCheckStates() {
        return mCheckStates.clone();
    }

    public void toggle(@IdRes int id) {
        View checkedView = findViewById(id);
        if (checkedView != null && checkedView instanceof CompoundButton) {
            toggleInGroup((CompoundButton) checkedView);
        }
    }

    private void toggleInGroup(CompoundButton buttonView) {
        final int id = buttonView.getId();
        boolean checkedStateChanged = updateCheckState(id);
        if (!checkedStateChanged) {
            Log.d(TAG, "toggleInGroup: check state not change, id:" + id);
            // 设置checkedView的checked状态与mCheckStates的一致
            mProtectFromCheckedChange = true;
            buttonView.setChecked(mCheckStates.get(id));
            mProtectFromCheckedChange = false;
        } else {
            updateCompoundButtons();
        }
    }

    private boolean updateCheckState(@IdRes int viewId) {
        boolean changed = true;
        if (mCheckMode == CHECK_MODE_SINGLE) {
            boolean checked = !mCheckStates.get(viewId, false);
            if (checked) {
                mCheckStates.clear();
                mCheckStates.put(viewId, true);
                mCheckedItemCount = 1;
            } else if (mCheckStates.size() == 0 || !mCheckStates.valueAt(0)) {
                mCheckedItemCount = 0;
            }
        } else if (mCheckMode == CHECK_MODE_MULTIPLE) {
            boolean checked = !mCheckStates.get(viewId, false);
            // 判断能否取消选中,不能取消的话重新设置为选中
            if (!checked && mCheckedItemCount - 1 < mMinCheckedCount) {
                changed = false;
            } else {
                mCheckStates.put(viewId, checked);
                if (checked) {
                    mCheckedItemCount++;
                } else {
                    mCheckedItemCount--;
                }
            }
        }
        return changed;
    }

    private void updateCompoundButtons() {
        mProtectFromCheckedChange = true;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof CompoundButton) {
                ((CompoundButton) child).setChecked(mCheckStates.get(child.getId(), false));
            }
        }
        mProtectFromCheckedChange = false;
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof CompoundButton) {
            final CompoundButton button = (CompoundButton) child;
            int id = child.getId();
            // generates an id if it's missing
            if (id == View.NO_ID) {
                id = ViewUtils.generateViewId();
                child.setId(id);
            }
            if (button.isChecked()) {
                toggleInGroup(button);
            }
            button.setOnCheckedChangeListener(mChildCheckedChangeListener);
        }
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (child instanceof CompoundButton) {
            ((CompoundButton) child).setOnCheckedChangeListener(null);
        }
    }
}