package com.benio.demoproject.usecase;

/**
 * Created by zhangzhibin on 2017/6/5.
 */
public class BusinessException extends UseCaseException {
    public BusinessException() {
        super();
    }

    public BusinessException(String detailMessage) {
        super(detailMessage);
    }

    public BusinessException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }
}
