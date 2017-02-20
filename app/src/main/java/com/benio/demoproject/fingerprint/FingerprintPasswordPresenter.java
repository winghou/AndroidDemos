package com.benio.demoproject.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by zhangzhibin on 2017/2/20.
 */

public class FingerprintPasswordPresenter implements FingerprintPasswordContract.Presenter {
    private FingerprintPasswordContract.View mView;

    private KeyguardManager mKeyguardManager;
    private FingerprintManagerCompat mFingerprintManager;
    private CryptoObjectCreator mCryptoObjectCreator;
    private FingerprintAuthHelper mFingerprintAuthHelper;

    public FingerprintPasswordPresenter(Context context, FingerprintPasswordContract.View view) {
        mView = view;
        mFingerprintManager = FingerprintManagerCompat.from(context);
        mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public void start() {
        if (isFingerprintAuthAvailable()) {
            mView.showAuthenticating();
            if (mCryptoObjectCreator == null) {
                mCryptoObjectCreator = new CryptoObjectCreator();
            }
            if (mFingerprintAuthHelper == null) {
                mFingerprintAuthHelper = new FingerprintAuthHelper(mFingerprintManager, new FingerprintAuthHelper.Callback() {
                    @Override
                    public void onAuthenticated() {
                        mView.showAuthSuccess();
                    }

                    @Override
                    public void onError() {
                        mView.showAuthFailure();
                    }
                });
            }
            mFingerprintAuthHelper.startListening(mCryptoObjectCreator.getCryptoObject());
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
        if (!mFingerprintManager.isHardwareDetected()) {
            mView.showNoHardwareDetected();
            return false;
        }

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && mKeyguardManager.isKeyguardSecure())) {
            // Show a message that the user hasn't set up a fingerprint or lock screen.
            mView.showKeyguardInsecure();
            return false;
        }

        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            mView.showNoEnrolledFingerprints();
            return false;
        }
        return true;
    }
}
