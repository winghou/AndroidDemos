package com.benio.demoproject.usecase;

/**
 * Created by zhangzhibin on 2017/5/3.
 */
public class UseCaseException extends Exception {

    public UseCaseException() {
    }

    public UseCaseException(String detailMessage) {
        super(detailMessage);
    }

    public UseCaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UseCaseException(Throwable throwable) {
        super(throwable);
    }
}
