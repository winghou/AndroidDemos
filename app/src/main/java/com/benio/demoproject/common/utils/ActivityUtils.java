package com.benio.demoproject.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {

    public static Activity getActivity(View view) {
        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    public static ActionBar getSupportActionBar(Activity activity) {
        return activity instanceof AppCompatActivity ?
                ((AppCompatActivity) activity).getSupportActionBar() :
                null;
    }

    public static void setWindowAlpha(Activity activity, float alpha) {
        if (null == activity) {
            return;
        }
        Window window = activity.getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.alpha = alpha;
            if (alpha == 1F) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
            }
            window.setAttributes(params);
        }
    }
}