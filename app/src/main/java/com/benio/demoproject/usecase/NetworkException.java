package com.benio.demoproject.usecase;

/**
 * Created by zhangzhibin on 2017/6/5.
 */
public class NetworkException extends UseCaseException {
    public NetworkException() {
        super();
    }

    public NetworkException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetworkException(Throwable throwable) {
        super(throwable);
    }
}
