package com.benio.demoproject.fingerprint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benio.demoproject.R;

/**
 * Created by zhangzhibin on 2017/3/3.
 */
public class FingerprintAuthenticationDialogFragment extends DialogFragment
        implements View.OnClickListener, FingerprintContract.AuthenticationView {
    /**
     * 登陆类型
     */
    public static final int TYPE_LOGIN = 1;
    /**
     * 验证类型
     */
    public static final int TYPE_VALIDATE = 1 << 1;
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    private static final long FAILURE_DELAY_MILLIS = 800;
    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mStageView.setText(R.string.label_use_touch_id_for_mag);
        }
    };
    private TextView mStageView;
    private View mValidateLoginPwdView;
    private FingerprintContract.Presenter mPresenter;
    private OnValidatePasswordListener mOnValidatePasswordListener;
    private AuthCallback mAuthCallback;
    private int mType = TYPE_VALIDATE;

    public interface OnValidatePasswordListener {
        void onConfirm(DialogFragment dialog);
    }

    public interface AuthCallback {
        void onAuthSuccess();

        void onAuthFailure();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fingerprint_authentication, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mStageView = (TextView) view.findViewById(R.id.tv_fingerprint_authentication_stage);
        mValidateLoginPwdView = view.findViewById(R.id.tv_fingerprint_authentication_validate_login_pwd);
        mValidateLoginPwdView.setOnClickListener(this);
        view.findViewById(R.id.tv_fingerprint_authentication_cancel).setOnClickListener(this);
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.stop();
        }
    }

    public void setAuthCallback(AuthCallback authCallback) {
        mAuthCallback = authCallback;
    }

    public void setOnValidatePasswordListener(OnValidatePasswordListener onValidatePasswordListener) {
        mOnValidatePasswordListener = onValidatePasswordListener;
    }

    public void setType(int type) {
        if (mType != type) {
            mType = type;
            showValidateLoginPwdView(type);
        }
    }

    private void showValidateLoginPwdView(int type) {
        if (mValidateLoginPwdView != null) {
            mValidateLoginPwdView.setVisibility(type == TYPE_LOGIN ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_fingerprint_authentication_validate_login_pwd) {
            if (mOnValidatePasswordListener != null) {
                mOnValidatePasswordListener.onConfirm(this);
            }
        } else if (id == R.id.tv_fingerprint_authentication_cancel) {
            dismiss();
        }
    }

    @Override
    public void showAuthSuccess() {
        mStageView.removeCallbacks(mResetErrorTextRunnable);
        mStageView.setText(R.string.msg_authenticate_success);
        mStageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuthCallback != null) {
                    mAuthCallback.onAuthSuccess();
                }
            }
        }, SUCCESS_DELAY_MILLIS);
    }

    @Override
    public void showAuthFailure() {
        showError(getString(R.string.try_again));
        mStageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuthCallback != null) {
                    mAuthCallback.onAuthFailure();
                }
            }
        }, FAILURE_DELAY_MILLIS);
        showValidateLoginPwdView(mType);
    }

    private void showError(CharSequence error) {
        if (!TextUtils.isEmpty(error)) {
            mStageView.setText(error);
        }
        mStageView.removeCallbacks(mResetErrorTextRunnable);
        mStageView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    @Override
    public void showAuthHelp(CharSequence helpMessage) {
        showError(helpMessage);
    }

    @Override
    public void showAuthError(CharSequence errString) {
        if (!TextUtils.isEmpty(errString)) {
            mStageView.setText(errString);
        }
        mStageView.removeCallbacks(mResetErrorTextRunnable);
        mStageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 收到Error后关闭对话框
                dismiss();
            }
        }, ERROR_TIMEOUT_MILLIS);
    }

    @Override
    public void setPresenter(FingerprintContract.Presenter presenter) {
        mPresenter = presenter;
    }
}