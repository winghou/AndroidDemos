package com.benio.demoproject.fingerprint;

/**
 * Created by zhangzhibin on 2017/3/8.
 */
public interface FingerprintContract {

    interface AuthenticationView extends BaseView<Presenter> {
        /**
         * 指纹认证成功
         */
        void showAuthSuccess();

        /**
         * 指纹认证失败
         */
        void showAuthFailure();

        /**
         * 指纹认证提示
         *
         * @param helpMessage
         */
        void showAuthHelp(CharSequence helpMessage);

        /**
         * 指纹认证错误
         *
         * @param errString
         */
        void showAuthError(CharSequence errString);
    }

    interface View extends BaseView<Presenter> {
        /**
         * 显示没有指纹识别权限
         */
        void showNoFingerprintPermission();

        /**
         * 显示系统当中没有指纹识别的硬件
         */
        void showNoHardwareDetected();

        /**
         * 显示设备不是处于安全保护中
         */
        void showKeyguardInsecure();

        /**
         * 系统中没有已注册的指纹信息
         */
        void showNoEnrolledFingerprints();
    }

    interface Presenter {
        /**
         * 指纹解锁是否可用
         *
         * @return
         */
        boolean isFingerprintAuthAvailable();

        /**
         * 开始监听
         */
        void start();

        /**
         * 停止监听
         */
        void stop();
    }
}
