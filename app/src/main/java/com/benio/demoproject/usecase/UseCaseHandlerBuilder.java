package com.benio.demoproject.usecase;

import java.util.concurrent.Executor;

/**
 * Created by zhangzhibin on 2017/5/5.
 */
public interface UseCaseHandlerBuilder {

    /**
     * @param executor Used for execute Runnable
     * @return this
     * @see {@link UseCaseHandler#execute(UseCase, UseCase.RequestValues, UseCase.UseCaseCallback)}
     */
    UseCaseHandlerBuilder executeOn(Executor executor);

    /**
     * @param poster Used for posting responses, typically to the main thread.
     * @return this
     * @see {@link UseCaseHandler#notifyResponse(UseCase.ResponseValue, UseCase.UseCaseCallback)}
     * @see {@link UseCaseHandler#notifyError(Exception, UseCase.UseCaseCallback)}
     */
    UseCaseHandlerBuilder responseOn(Executor poster);


    UseCaseHandler build();
}
