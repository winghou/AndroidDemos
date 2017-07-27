package com.benio.demoproject.usecase;

/**
 * Created by zhangzhibin on 2017/4/28.
 */
public abstract class BaseUseCase<Q extends UseCase.RequestValues, P extends UseCase.ResponseValue> extends UseCase<Q, P> {

    protected void notifySuccess(final P response) {
        UseCaseCallback<P> callback = getUseCaseCallback();
        if (callback != null) {
            callback.onSuccess(response);
        }
    }

    protected void notifyError(final Exception e) {
        UseCaseCallback<P> callback = getUseCaseCallback();
        if (callback != null) {
            callback.onError(e);
        }
    }
}
