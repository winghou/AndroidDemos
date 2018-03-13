package com.benio.demoproject.compoundbutton;

import android.support.annotation.NonNull;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.benio.demoproject.R;

/**
 * 为RadioGroup创建多个"Tab"样式的RadioButton
 * Created by zhangzhibin on 2018/3/8.
 */
public class TabGroupHelper extends RadioGroupHelper {
    private int mTabBackgroundForSingle = 0;
    private int mTabBackgroundForStart = 0;
    private int mTabBackgroundForEnd = 0;
    private int mTabBackgroundForMiddle = 0;

    public TabGroupHelper() {
        // 设置"Tab"的默认布局
        setRadioLayoutResource(R.layout.item_tab_group);
    }

    @Override
    public TabGroupHelper setRadioLayoutResource(int radioLayoutResource) {
        super.setRadioLayoutResource(radioLayoutResource);
        return this;
    }

    @Override
    public TabGroupHelper setCheckedItem(int checkedItem) {
        super.setCheckedItem(checkedItem);
        return this;
    }

    @Override
    public TabGroupHelper addItems(CharSequence... items) {
        super.addItems(items);
        return this;
    }

    @Override
    public TabGroupHelper addItem(CharSequence item) {
        super.addItem(item);
        return this;
    }

    @Override
    public TabGroupHelper clearItems() {
        super.clearItems();
        return this;
    }

    public TabGroupHelper setTabBackgroundForEnd(int tabBackgroundForEnd) {
        mTabBackgroundForEnd = tabBackgroundForEnd;
        return this;
    }

    public TabGroupHelper setTabBackgroundForMiddle(int tabBackgroundForMiddle) {
        mTabBackgroundForMiddle = tabBackgroundForMiddle;
        return this;
    }

    public TabGroupHelper setTabBackgroundForSingle(int tabBackgroundForSingle) {
        mTabBackgroundForSingle = tabBackgroundForSingle;
        return this;
    }

    public TabGroupHelper setTabBackgroundForStart(int tabBackgroundForStart) {
        mTabBackgroundForStart = tabBackgroundForStart;
        return this;
    }

    public TabGroupHelper setTabBackground(int single, int start, int end, int middle) {
        mTabBackgroundForSingle = single;
        mTabBackgroundForStart = start;
        mTabBackgroundForEnd = end;
        mTabBackgroundForMiddle = middle;
        return this;
    }

    @Override
    public void attachTo(@NonNull RadioGroup radioGroup) {
        if (mTabBackgroundForSingle == 0) {
            mTabBackgroundForSingle = R.drawable.s_tabgroup_item_bg;
        }
        if (mTabBackgroundForStart == 0) {
            mTabBackgroundForStart = R.drawable.s_tabgroup_item_bg;
        }
        if (mTabBackgroundForEnd == 0) {
            mTabBackgroundForEnd = R.drawable.s_tabgroup_item_bg;
        }
        if (mTabBackgroundForMiddle == 0) {
            mTabBackgroundForMiddle = R.drawable.s_tabgroup_item_bg;
        }
        super.attachTo(radioGroup);
    }

    @NonNull
    @Override
    protected RadioButton makeRadioButton(RadioGroup radioGroup, int itemCount, int index) {
        RadioButton radioButton = super.makeRadioButton(radioGroup, itemCount, index);
        radioButton.setButtonDrawable(null);
        int resDrawableId;
        if (itemCount == 1) {
            resDrawableId = mTabBackgroundForSingle;
        } else if (index == 0) {
            resDrawableId = mTabBackgroundForStart;
        } else if (index == itemCount - 1) {
            resDrawableId = mTabBackgroundForEnd;
        } else {
            resDrawableId = mTabBackgroundForMiddle;
        }
        radioButton.setBackgroundResource(resDrawableId);
        return radioButton;
    }
}
