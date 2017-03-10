package com.benio.demoproject.fingerprint;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

public class FingerprintAuthHelper extends FingerprintManagerCompat.AuthenticationCallback {
    private boolean mSelfCancelled;
    private CancellationSignal mCancellationSignal;
    private final FingerprintManagerCompat mFingerprintManager;
    private FingerprintManagerCompat.AuthenticationCallback mCallback;

    public FingerprintAuthHelper(FingerprintManagerCompat fingerprintManager) {
        mFingerprintManager = fingerprintManager;
    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject, FingerprintManagerCompat.AuthenticationCallback callback) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCallback = callback;
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        mFingerprintManager.authenticate(cryptoObject, 0, mCancellationSignal, this, null);
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
        mCallback = null;
    }

    // 多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证,一般间隔从几秒到几十秒不等
    // 这种情况不建议重试，建议提示用户用其他的方式解锁或者认证
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            mCallback.onAuthenticationError(errMsgId, errString);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        if (mCallback != null) {
            mCallback.onAuthenticationHelp(helpMsgId, helpString);
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        if (mCallback != null) {
            mCallback.onAuthenticationSucceeded(result);
        }
    }

    @Override
    public void onAuthenticationFailed() {
        if (mCallback != null) {
            mCallback.onAuthenticationFailed();
        }
    }
}