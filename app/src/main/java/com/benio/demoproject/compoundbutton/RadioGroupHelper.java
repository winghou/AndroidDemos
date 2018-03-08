package com.benio.demoproject.compoundbutton;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 为RadioGroup创建多个RadioButton
 * Created by zhangzhibin on 2018/3/7.
 */
public class RadioGroupHelper {
    private List<CharSequence> mItems;
    private int mRadioLayoutResource;
    private int mCheckedItem = -1;

    public RadioGroupHelper() {
        mItems = new ArrayList<>();
    }

    /**
     * 设置RadioButton布局
     *
     * @param radioLayoutResource
     * @return
     */
    public RadioGroupHelper setRadioLayoutResource(int radioLayoutResource) {
        mRadioLayoutResource = radioLayoutResource;
        return this;
    }

    /**
     * 设置选中项
     *
     * @param checkedItem 选中项下标
     * @return
     */
    public RadioGroupHelper setCheckedItem(int checkedItem) {
        mCheckedItem = checkedItem;
        return this;
    }

    public int getRadioLayoutResource() {
        return mRadioLayoutResource;
    }

    /**
     * 添加多个单选项
     *
     * @param items
     * @return
     */
    public RadioGroupHelper addItems(CharSequence... items) {
        for (CharSequence item : items) {
            addItem(item);
        }
        return this;
    }

    /**
     * 添加一个单选项
     *
     * @param item 单选项文字
     * @return
     */
    public RadioGroupHelper addItem(CharSequence item) {
        if (item != null) {
            mItems.add(item);
        }
        return this;
    }

    public void attachTo(@NonNull RadioGroup radioGroup) {
        final int itemCount = mItems.size();
        for (int i = 0; i < itemCount; i++) {
            CharSequence item = mItems.get(i);
            RadioButton radioButton = makeRadioButton(radioGroup, itemCount, i);
            radioButton.setText(item);
            radioButton.setChecked(mCheckedItem == i);
            radioGroup.addView(radioButton);
        }
    }

    @NonNull
    protected RadioButton makeRadioButton(RadioGroup radioGroup, int itemCount, int index) {
        RadioButton radioButton;
        if (mRadioLayoutResource != 0) {
            radioButton = (RadioButton) LayoutInflater.from(radioGroup.getContext())
                    .inflate(mRadioLayoutResource, radioGroup, false);
        } else {
            radioButton = new RadioButton(radioGroup.getContext());
        }
        radioButton.setId(index);
        return radioButton;
    }
}
