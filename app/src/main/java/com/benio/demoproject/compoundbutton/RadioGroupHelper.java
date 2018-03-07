package com.benio.demoproject.compoundbutton;

import android.content.Context;
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

    public RadioGroupHelper() {
        mItems = new ArrayList<>();
    }

    public RadioGroupHelper setRadioLayoutResource(int radioLayoutResource) {
        mRadioLayoutResource = radioLayoutResource;
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
        mItems.add(item);
        return this;
    }

    public void attachTo(RadioGroup radioGroup) {
        final Context context = radioGroup.getContext();
        int itemCount = mItems.size();
        for (int i = 0; i < itemCount; i++) {
            CharSequence item = mItems.get(i);
            RadioButton radioButton = makeRadioButton(context, itemCount, i);
            radioButton.setText(item);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }
    }

    protected RadioButton makeRadioButton(Context context, int itemCount, int index) {
        RadioButton radioButton = null;
        if (mRadioLayoutResource != 0) {
            radioButton = (RadioButton) LayoutInflater.from(context)
                    .inflate(mRadioLayoutResource, null);
        } else {
            radioButton = new RadioButton(context);
            RadioGroup.LayoutParams p =
                    new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            p.weight = 1;
            radioButton.setLayoutParams(p);
        }
        return radioButton;
    }
}
