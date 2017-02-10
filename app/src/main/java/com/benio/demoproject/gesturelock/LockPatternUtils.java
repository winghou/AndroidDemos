/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.benio.demoproject.gesturelock;


import android.content.Context;
import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for the lock pattern and its settings.
 */
public class LockPatternUtils {

    /**
     * Deserialize a pattern.
     *
     * @param string The pattern serialized with {@link #patternToString}
     * @return The pattern.
     */
    public static List<LockPatternView.Cell> stringToPattern(String string) {
        List<LockPatternView.Cell> result = new ArrayList<>();

        final byte[] bytes = string.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            result.add(LockPatternView.Cell.of(b / 3, b % 3));
        }
        return result;
    }

    /**
     * Serialize a pattern.
     *
     * @param pattern The pattern.
     * @return The pattern in string form.
     */
    public static String patternToString(List<LockPatternView.Cell> pattern) {
        if (pattern == null) {
            return "";
        }
        final int patternSize = pattern.size();

        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            LockPatternView.Cell cell = pattern.get(i);
            res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
        }
        return new String(res);
    }

    /**
     * AccessibilityUtils provides functions needed in accessibility mode. All the functions
     * in this class are made compatible with gingerbread and later API's
     */
    public static void announceForAccessibilityCompat(View view, CharSequence announcement) {
        if (view == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.announceForAccessibility(announcement);
        } else {
            // For API 15 and earlier, we need to construct an accessibility event
            Context ctx = view.getContext();
            AccessibilityManager am = (AccessibilityManager) ctx.getSystemService(
                    Context.ACCESSIBILITY_SERVICE);
            if (!am.isEnabled()) return;
            AccessibilityEvent event = AccessibilityEvent.obtain(
                    AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
            AccessibilityRecordCompat arc = new AccessibilityRecordCompat(event);
            arc.setSource(view);
            event.setClassName(view.getClass().getName());
            event.setPackageName(view.getContext().getPackageName());
            event.setEnabled(view.isEnabled());
            event.getText().add(announcement);
            am.sendAccessibilityEvent(event);
        }
    }
}