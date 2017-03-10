package com.benio.demoproject.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by zhangzhibin on 2017/3/8.
 */
public class FingerprintPresenter implements FingerprintContract.Presenter {
    private FingerprintContract.View mView;
    private FingerprintContract.AuthenticationView mAuthenticationView;
    private KeyguardManager mKeyguardManager;
    private FingerprintManagerCompat mFingerprintManager;
    private CryptoObjectCreator mCryptoObjectCreator;
    private FingerprintAuthHelper mFingerprintAuthHelper;
    private FingerprintManagerCompat.AuthenticationCallback mAuthCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        private int mRetryCount;

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            mAuthenticationView.showAuthError(errString);
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            mAuthenticationView.showAuthHelp(helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            mAuthenticationView.showAuthSuccess();
        }

        @Override
        public void onAuthenticationFailed() {
            mRetryCount++;
            mAuthenticationView.showAuthFailure();
            if (mOnRetryFingerprintAuthListener != null && mOnRetryFingerprintAuthListener.onRetryFingerprintAuth(mRetryCount)) {
                mRetryCount = 0;
            }
        }
    };

    private OnRetryFingerprintAuthListener mOnRetryFingerprintAuthListener;

    public interface OnRetryFingerprintAuthListener {
        /**
         * @param retryCount 重试次数
         * @return true:重置 retryCount
         */
        boolean onRetryFingerprintAuth(int retryCount);
    }

    public FingerprintPresenter(Context context, FingerprintContract.View view, FingerprintContract.AuthenticationView authenticationView) {
        this(context, view, authenticationView, null);
    }

    public FingerprintPresenter(Context context, FingerprintContract.View view, FingerprintContract.AuthenticationView authenticationView, OnRetryFingerprintAuthListener onRetryListener) {
        mView = view;
        mAuthenticationView = authenticationView;
        mOnRetryFingerprintAuthListener = onRetryListener;
        mView.setPresenter(this);
        mAuthenticationView.setPresenter(this);
        mFingerprintManager = FingerprintManagerCompat.from(context);
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public void start() {
        if (isFingerprintAuthAvailable()) {
            try {
                if (mCryptoObjectCreator == null) {
                    mCryptoObjectCreator = new CryptoObjectCreator();
                }
            } catch (Exception e) {
                e.printStackTrace();
                mAuthenticationView.showAuthError(null);
                return;
            }
            if (mFingerprintAuthHelper == null) {
                mFingerprintAuthHelper = new FingerprintAuthHelper(mFingerprintManager);
            }
            try {
                mFingerprintAuthHelper.startListening(mCryptoObjectCreator.getCryptoObject(), mAuthCallback);
            } catch (Exception e) {
                e.printStackTrace();
                mAuthenticationView.showAuthError(null);
            }
        }
    }

    @Override
    public void stop() {
        if (mFingerprintAuthHelper != null) {
            mFingerprintAuthHelper.stopListening();
        }
    }

    @Override
    public boolean isFingerprintAuthAvailable() {
        boolean isHardwareDetected = false;
        try {
            isHardwareDetected = mFingerprintManager.isHardwareDetected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isHardwareDetected) {
            mView.showNoHardwareDetected();
            return false;
        }

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && mKeyguardManager.isKeyguardSecure())) {
            // Show a message that the user hasn't set up a fingerprint or lock screen.
            mView.showKeyguardInsecure();
            return false;
        }

        boolean hasEnrolledFingerprints = false;
        try {
            hasEnrolledFingerprints = mFingerprintManager.hasEnrolledFingerprints();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!hasEnrolledFingerprints) {
            mView.showNoEnrolledFingerprints();
            return false;
        }
        return true;
    }
}
