package com.benio.demoproject.fingerprint;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

public class FingerprintAuthHelper extends FingerprintManagerCompat.AuthenticationCallback {
    private boolean mSelfCancelled;
    private CancellationSignal mCancellationSignal;
    private final FingerprintManagerCompat mFingerprintManager;
    private Callback mCallback;

    public interface Callback {

        void onAuthenticated();

        void onError();
    }

    public FingerprintAuthHelper(FingerprintManagerCompat fingerprintManager, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mCallback = callback;
    }

    public boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
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
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            notifyAuthError();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        notifyAuthError();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        if (mCallback != null) {
            mCallback.onAuthenticated();
        }
    }

    @Override
    public void onAuthenticationFailed() {
        notifyAuthError();
    }

    private void notifyAuthError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }
}