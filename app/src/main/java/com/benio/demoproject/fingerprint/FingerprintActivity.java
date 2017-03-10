package com.benio.demoproject.fingerprint;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.benio.demoproject.R;

public class FingerprintActivity extends AppCompatActivity {
    private static final String TAG = "FingerprintActivity";
    private static final String DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG";
    /**
     * 指纹密码最大错重试次数
     */
    private static final int MAX_RETRY_COUNT = 10;

    private FingerprintAuthenticationDialogFragment mDialogFragment;
    private FingerprintContract.Presenter mFingerprintPresenter;
    private FingerprintViewHelper mFingerprintViewHelper;
    private FingerprintPresenter.OnRetryFingerprintAuthListener mOnRetryFingerprintAuthListener =
            new FingerprintPresenter.OnRetryFingerprintAuthListener() {
                @Override
                public boolean onRetryFingerprintAuth(int retryCount) {
                    Log.d(TAG, "onRetryFingerprintAuth: " + retryCount);
                    // 验证指纹重试次数大于最大值时，需要跳转登陆页面验证密码
                    if (retryCount >= MAX_RETRY_COUNT) {
                        Toast.makeText(FingerprintActivity.this, "密码错误次数过多，次数：" + retryCount, Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFingerprintAuth();
            }
        });
        mFingerprintViewHelper = new FingerprintViewHelper(this);
        mDialogFragment = new FingerprintAuthenticationDialogFragment();
        mDialogFragment.setOnValidatePasswordListener(new FingerprintAuthenticationDialogFragment.OnValidatePasswordListener() {
            @Override
            public void onConfirm(DialogFragment dialog) {
                dialog.dismiss();
            }
        });
        mDialogFragment.setAuthCallback(new FingerprintAuthenticationDialogFragment.AuthCallback() {

            @Override
            public void onAuthSuccess() {
                Toast.makeText(FingerprintActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailure() {
                Toast.makeText(FingerprintActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
            }
        });
        mFingerprintPresenter = new FingerprintPresenter(this, mFingerprintViewHelper, mDialogFragment, mOnRetryFingerprintAuthListener);
        requestFingerprintAuth();
    }

    private void requestFingerprintAuth() {
        if (mFingerprintViewHelper.isFingerprintAuthAvailable()) {
            mDialogFragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FingerprintViewHelper.REQUEST_FINGERPRINT_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "USE_FINGERPRINT Permission is granted at Runtime", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "USE_FINGERPRINT Permission is denied at Runtime", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
