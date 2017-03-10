package com.benio.demoproject.fingerprint;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private static final long RESET_DELAY_MILLIS = 900;
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    private static final long FAILURE_DELAY_MILLIS = 800;
    private static final int MESSAGE_RESET = 1;
    private static final int MESSAGE_ERROR = 1 << 1;
    private static final int MESSAGE_SUCCESS = 1 << 2;
    private static final int MESSAGE_FAILURE = 1 << 3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RESET:
                    mStageView.setText(R.string.label_use_touch_id_for_mag);
                    break;
                case MESSAGE_SUCCESS:
                    if (mAuthCallback != null) {
                        mAuthCallback.onAuthSuccess();
                    }
                    break;
                case MESSAGE_FAILURE:
                    if (mAuthCallback != null) {
                        mAuthCallback.onAuthFailure();
                    }
                    break;
                case MESSAGE_ERROR:
                    // 收到Error后关闭对话框
                    dismiss();
                    break;

            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MESSAGE_ERROR);
        mHandler.removeMessages(MESSAGE_FAILURE);
        mHandler.removeMessages(MESSAGE_SUCCESS);
        mHandler.removeMessages(MESSAGE_RESET);
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
        mStageView.setText(R.string.msg_authenticate_success);
        mHandler.removeMessages(MESSAGE_SUCCESS);
        mHandler.sendEmptyMessageDelayed(MESSAGE_SUCCESS, SUCCESS_DELAY_MILLIS);
    }

    @Override
    public void showAuthFailure() {
        showError(getString(R.string.try_again));
        showValidateLoginPwdView(mType);
        mHandler.removeMessages(MESSAGE_FAILURE);
        mHandler.sendEmptyMessageDelayed(MESSAGE_FAILURE, FAILURE_DELAY_MILLIS);
    }

    private void showError(CharSequence error) {
        if (!TextUtils.isEmpty(error)) {
            mStageView.setText(error);
        }
        mHandler.removeMessages(MESSAGE_RESET);
        mHandler.sendEmptyMessageDelayed(MESSAGE_RESET, RESET_DELAY_MILLIS);
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
        mHandler.removeMessages(MESSAGE_ERROR);
        mHandler.sendEmptyMessageDelayed(MESSAGE_ERROR, ERROR_TIMEOUT_MILLIS);
    }

    @Override
    public void setPresenter(FingerprintContract.Presenter presenter) {
        mPresenter = presenter;
    }
}