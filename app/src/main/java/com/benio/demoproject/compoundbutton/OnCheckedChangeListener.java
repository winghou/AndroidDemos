package com.benio.demoproject.compoundbutton;

import android.support.annotation.IdRes;
import android.view.ViewGroup;

/**
 * <p>Interface definition for a callback to be invoked when the checked
 * checkbox changed in this group.</p>
 */
public interface OnCheckedChangeListener {

    void onCheckedChanged(ViewGroup group, @IdRes int checkedId, boolean isChecked);
}