package com.benio.demoproject.common.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by zhangzhibin on 2017/10/11.
 */
public class ScreenUtils {

    public static float dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    public static float pxToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / metrics.density;
    }
}
