package com.benio.demoproject.db;

import android.provider.BaseColumns;

/**
 * Created by zhangzhibin on 2017/2/27.
 */
public interface PasswordManagementContract {

    interface Entry extends BaseColumns {
        String TABLE_NAME = "password";
        String COLUMN_NAME_USER_ID = "user_id";
        String COLUMN_NAME_GESTURE_ENABLE = "gesture";
        String COLUMN_NAME_FINGERPRINT_ENABLE = "fingerprint";
        String COLUMN_NAME_GESTURE_PASSWORD = "gesture_pwd";
    }

    void setGestureEnable(boolean enable);

    boolean isGestureEnable();

    void setFingerprintEnable(boolean enable);

    boolean isFingerprintEnable();

    String getGesturePassword();

    void setGesturePassword(String password);
}
