package com.benio.demoproject.fingerprint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benio.demoproject.R;

import java.util.Arrays;

public class FingerprintActivity extends AppCompatActivity implements FingerprintPasswordContract.View {
    public static final int REQUEST_PERMISSION = 1002;
    private Button mStartBtn;
    private Button mCancelBtn;
    private TextView mStatusView;
    private FingerprintPasswordContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        mStatusView = (TextView) findViewById(R.id.tv_status);
        mStartBtn = (Button) findViewById(R.id.btn_start);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.stop();
                mStatusView.setText("认证已取消");
            }
        });

        mPresenter = new FingerprintPasswordPresenter(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    private void showMessage(CharSequence charSequence) {
        Toast.makeText(this, charSequence, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoFingerprintPermission() {
        showMessage("请先开启指纹权限");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                REQUEST_PERMISSION);
    }

    @Override
    public void showNoHardwareDetected() {
        showMessage("showNoHardwareDetected");
    }

    private static final String TAG = "xxxx";

    @Override
    public void showKeyguardInsecure() {
        showMessage("请先开启屏幕锁");
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void showNoEnrolledFingerprints() {
        showMessage("请先录入指纹");
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void showAuthenticating() {
        mStatusView.setText("认证中...");
    }

    @Override
    public void showAuthSuccess() {
        mStatusView.setText("认证成功");
    }

    @Override
    public void showAuthFailure() {
        mStatusView.setText("认证失败");
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            showNoFingerprintPermission();
        } else {
            mPresenter.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + Arrays.toString(permissions) + "], grantResults = [" + Arrays.toString(grantResults) + "]");
        switch (requestCode) {
            case REQUEST_PERMISSION:
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
