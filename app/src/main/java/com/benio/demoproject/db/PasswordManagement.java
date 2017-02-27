package com.benio.demoproject.db;

/**
 * Created by zhangzhibin on 2017/2/27.
 */
public class PasswordManagement {
    private int userId;
    private boolean gestureEnable;
    private boolean fingerprintEnable;
    private String gesturePassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isGestureEnable() {
        return gestureEnable;
    }

    public void setGestureEnable(boolean gestureEnable) {
        this.gestureEnable = gestureEnable;
    }

    public boolean isFingerprintEnable() {
        return fingerprintEnable;
    }

    public void setFingerprintEnable(boolean fingerprintEnable) {
        this.fingerprintEnable = fingerprintEnable;
    }

    public String getGesturePassword() {
        return gesturePassword;
    }

    public void setGesturePassword(String gesturePassword) {
        this.gesturePassword = gesturePassword;
    }

    @Override
    public String toString() {
        return "PasswordManagement{" +
                "userId=" + userId +
                ", gestureEnable=" + gestureEnable +
                ", fingerprintEnable=" + fingerprintEnable +
                ", gesturePassword='" + gesturePassword + '\'' +
                '}';
    }
}
