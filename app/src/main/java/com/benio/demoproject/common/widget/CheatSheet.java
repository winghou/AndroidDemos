package com.benio.demoproject.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * from {@link android.support.v7.view.menu.ActionMenuItemView }
 * <p>
 * Created by zhangzhibin on 2017/6/16.
 */
public class CheatSheet {

    public static void attachTo(final @NonNull View view, final CharSequence text) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return show(v, text);
            }
        });
    }

    private static boolean show(final View view, final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            // Don't show the cheat sheet for items that already show text.
            return false;
        }

        final int[] screenPos = new int[2];
        final Rect displayFrame = new Rect();
        view.getLocationOnScreen(screenPos);
        view.getWindowVisibleDisplayFrame(displayFrame);

        final Context context = view.getContext();
        final int width = view.getWidth();
        final int height = view.getHeight();
        final int midy = screenPos[1] + height / 2;
        int referenceX = screenPos[0] + width / 2;
        if (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR) {
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            referenceX = screenWidth - referenceX; // mirror
        }
        Toast cheatSheet = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        if (midy < displayFrame.height()) {
            // Show along the top; follow action buttons
            cheatSheet.setGravity(Gravity.TOP | GravityCompat.END, referenceX,
                    screenPos[1] + height - displayFrame.top);
        } else {
            // Show along the bottom center
            cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
        }
        cheatSheet.show();
        return true;
    }
}
