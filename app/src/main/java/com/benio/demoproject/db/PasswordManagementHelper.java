package com.benio.demoproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangzhibin on 2017/2/27.
 */
public class PasswordManagementHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PasswordManagement.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            PasswordManagementContract.Entry.TABLE_NAME + " (" +
            PasswordManagementContract.Entry._ID + " INTEGER PRIMARY KEY," +
            PasswordManagementContract.Entry.COLUMN_NAME_USER_ID + " TEXT" + "," +
            PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_ENABLE + " TEXT" + "," +
            PasswordManagementContract.Entry.COLUMN_NAME_FINGERPRINT_ENABLE + " TEXT" + "," +
            PasswordManagementContract.Entry.COLUMN_NAME_GESTURE_PASSWORD + " TEXT" + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PasswordManagementContract.Entry.TABLE_NAME;

    public PasswordManagementHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
