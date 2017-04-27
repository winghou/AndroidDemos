package com.benio.demoproject.common;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * 根据id找到对应的View
 * Created by benio on 2016/1/15.
 */
public interface ViewFinder {
    /**
     * 根据view id返回View
     *
     * @param id id resource
     * @return null or specific view
     */
    <T extends View> T getView(@IdRes int id);
}