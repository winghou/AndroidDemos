package com.benio.demoproject.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * Created by zhangzhibin on 2017/2/27.
 */
public class PasswordManagementDao extends PasswordManagementHelper implements PasswordManagementContract {
    private static final String KEY = "91c5bRcX5SSUAZ8x";
    private static PasswordManagementDao sInstance;
    private PasswordManagement mPasswordManagement;
    private long mId;

    public static PasswordManagementDao getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PasswordManagementDao(context);
        }
        return sInstance;
    }

    public PasswordManagementDao(Context context) {
        super(context);
    }

    public PasswordManagementDao(Context context, int userId) {
        super(context);
        setUserId(userId);
    }

    public void setUserId(int userId) {
        mPasswordManagement = query(userId);
        if (mPasswordManagement == null) {
            mPasswordManagement = new PasswordManagement();
            mPasswordManagement.setUserId(userId);
        }
    }

    private PasswordManagement query(int userId) {
        PasswordManagement result = null;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String[] projection = {
                    PasswordManagementContract.Entry._ID,
                    PasswordManagementContract.Entry.COLUMN_NAME_USER_ID,
                    PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_ENABLE,
                    PasswordManagementContract.Entry.COLUMN_NAME_FINGERPRINT_ENABLE,
                    PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_PASSWORD,
            };
            String selection = PasswordManagementContract.Entry.COLUMN_NAME_USER_ID + " = ?";
            String[] selectionArgs = {String.valueOf(userId)};
            cursor = db.query(PasswordManagementContract.Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                mId = cursor.getLong(cursor.getColumnIndex(PasswordManagementContract.Entry._ID));
                result = new PasswordManagement();
                result.setUserId(cursor.getInt(cursor.getColumnIndex(PasswordManagementContract.Entry.COLUMN_NAME_USER_ID)));
                result.setGestureEnable(cursor.getInt(cursor.getColumnIndex(PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_ENABLE)) != 0);
                result.setFingerprintEnable(cursor.getInt(cursor.getColumnIndex(PasswordManagementContract.Entry.COLUMN_NAME_FINGERPRINT_ENABLE)) != 0);
                result.setGesturePassword(cursor.getString(cursor.getColumnIndex(PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_PASSWORD)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }
        return result;
    }

    private void update(PasswordManagement passwordManagement) {
        if (mId == 0) {
            save(passwordManagement);
            return;
        }
        try {
            SQLiteDatabase db = getReadableDatabase();
            String selection = PasswordManagementContract.Entry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(mId)};
            ContentValues values = new ContentValues();
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_FINGERPRINT_ENABLE, passwordManagement.isFingerprintEnable());
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_ENABLE, passwordManagement.isGestureEnable());
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_PASSWORD, passwordManagement.getGesturePassword());
            db.update(PasswordManagementContract.Entry.TABLE_NAME, values, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void save(PasswordManagement passwordManagement) {
        if (mId != 0) {
            update(passwordManagement);
            return;
        }
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_USER_ID, passwordManagement.getUserId());
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_FINGERPRINT_ENABLE, passwordManagement.isFingerprintEnable());
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_ENABLE, passwordManagement.isGestureEnable());
            values.put(PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_PASSWORD, passwordManagement.getGesturePassword());
            db.insert(PasswordManagementContract.Entry.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void setGestureEnable(boolean enable) {
        mPasswordManagement.setGestureEnable(enable);
        update(mPasswordManagement);
    }

    public boolean isGestureEnable() {
        return mPasswordManagement.isGestureEnable();
    }

    public void setFingerprintEnable(boolean enable) {
        mPasswordManagement.setFingerprintEnable(enable);
        update(mPasswordManagement);
    }

    public boolean isFingerprintEnable() {
        return mPasswordManagement.isFingerprintEnable();
    }

    public String getGesturePassword() {
        return decrypt(mPasswordManagement.getGesturePassword());
    }

    public void setGesturePassword(String password) {
        mPasswordManagement.setGesturePassword(encrypt(password));
        update(mPasswordManagement);
    }

    private static String decrypt(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = Des3Util.decode(KEY, str);
        }
        return str;
    }

    private static String encrypt(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = Des3Util.encode(KEY, str);
        }
        return str;
    }
}
