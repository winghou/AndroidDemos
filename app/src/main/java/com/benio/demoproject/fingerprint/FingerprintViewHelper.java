package com.benio.demoproject.fingerprint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.benio.demoproject.R;

/**
 * Created by zhangzhibin on 2017/3/8.
 */
public class FingerprintViewHelper implements FingerprintContract.View {
    /**
     * 请求指纹权限
     */
    public static final int REQUEST_FINGERPRINT_PERMISSION = 0x700;
    private FingerprintContract.Presenter mPresenter;
    private Activity mActivity;

    public FingerprintViewHelper(Activity activity) {
        mActivity = activity;
    }

    public static boolean hasFingerprintPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @Override
    public void showNoFingerprintPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.USE_FINGERPRINT},
                    REQUEST_FINGERPRINT_PERMISSION);
        } else {
            showMessage(R.string.msg_open_fingerprint_permission);
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void showNoHardwareDetected() {
        showMessage(R.string.error_open_fingerprint_password);
        mActivity.finish();
    }

    @Override
    public void showKeyguardInsecure() {
        showMessage(R.string.msg_open_keyguard);
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        mActivity.startActivity(intent);
    }

    @Override
    public void showNoEnrolledFingerprints() {
        showMessage(R.string.msg_no_enrolled_fingerprints);
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        mActivity.startActivity(intent);
    }

    @Override
    public void setPresenter(FingerprintContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public boolean isFingerprintAuthAvailable() {
        if (!FingerprintViewHelper.hasFingerprintPermission(mActivity)) {
            showNoFingerprintPermission();
            return false;
        }
        return mPresenter != null && mPresenter.isFingerprintAuthAvailable();
    }

    private void showMessage(int resId) {
        Toast.makeText(mActivity.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
