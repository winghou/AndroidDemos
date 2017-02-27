package com.benio.demoproject.db;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.benio.demoproject.R;

public class SQLiteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView mDataView;
    private Switch mGestureSwitch;
    private Switch mFingerprintSwitch;

    private PasswordManagementDao mPasswordManagementDao;
    private int mUserId = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        mDataView = (TextView) findViewById(R.id.tv_data);
        Switch gestureSwitch = (Switch) findViewById(R.id.switch_gesture);
        gestureSwitch.setOnCheckedChangeListener(this);
        mGestureSwitch = gestureSwitch;

        Switch fingerprintSwitch = (Switch) findViewById(R.id.switch_fingerprint);
        fingerprintSwitch.setOnCheckedChangeListener(this);
        mFingerprintSwitch = fingerprintSwitch;

        findViewById(R.id.btn_change_user).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        mPasswordManagementDao = new PasswordManagementDao(this, mUserId);
        showData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_user:
                mUserId++;
                mPasswordManagementDao = new PasswordManagementDao(this, mUserId);
                showData();
                break;

            case R.id.btn_search:
                showData();
                break;
        }
    }

    private void showData() {
        PasswordManagementContract passwordManagement = mPasswordManagementDao;
        boolean gestureEnable = passwordManagement.isGestureEnable();
        boolean fingerprintEnable = passwordManagement.isFingerprintEnable();
        mDataView.setText("gesture:" + gestureEnable
                + "\nfingerprint:" + fingerprintEnable
                + "\npwd:" + mPasswordManagementDao.getGesturePassword()
                + "\nuserId:" + mUserId);
        setCheckedWithoutNotifyListener(mGestureSwitch, gestureEnable);
        setCheckedWithoutNotifyListener(mFingerprintSwitch, fingerprintEnable);
    }

    private void setCheckedWithoutNotifyListener(CompoundButton compoundButton, boolean checked) {
        compoundButton.setOnCheckedChangeListener(null);
        compoundButton.setChecked(checked);
        compoundButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.switch_gesture) {
            if (isChecked) {
                mPasswordManagementDao.setGestureEnable(true);
                mPasswordManagementDao.setGesturePassword(String.valueOf(mUserId));
            } else {
                mPasswordManagementDao.setGestureEnable(false);
                mPasswordManagementDao.setGesturePassword(null);
            }
        } else if (id == R.id.switch_fingerprint) {
            mPasswordManagementDao.setFingerprintEnable(isChecked);
        }
        showData();
    }
}
