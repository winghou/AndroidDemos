package com.benio.demoproject.usecase;

import java.util.concurrent.Executor;

/**
 * Created by zhangzhibin on 2017/5/5.
 */
public class DefaultUseCaseHandlerBuilder implements UseCaseHandlerBuilder {
    private static UseCaseHandlerBuilder sInstance;
    // default UseCaseHandlers
    private static UseCaseHandler DEFAULT_HANDLER;
    private static UseCaseHandler DEFAULT_BACKGROUND_HANDLER;

    private Executor mExecutor;
    private Executor mResponsePoster;

    public static UseCaseHandlerBuilder getInstance() {
        if (sInstance == null) {
            sInstance = new DefaultUseCaseHandlerBuilder();
        }
        return sInstance;
    }

    @Override
    public UseCaseHandlerBuilder executeOn(Executor executor) {
        mExecutor = executor;
        return this;
    }

    @Override
    public UseCaseHandlerBuilder responseOn(Executor poster) {
        mResponsePoster = poster;
        return this;
    }

    @Override
    public UseCaseHandler build() {
        if (mExecutor == null) {
            mExecutor = ExecutorFactory.immediate();
        }
        if (mResponsePoster == null) {
            mResponsePoster = ExecutorFactory.ui();
        }

        UseCaseHandler useCaseHandler = null;
        if (mExecutor.equals(ExecutorFactory.immediate()) && mResponsePoster.equals(ExecutorFactory.ui())) {
            useCaseHandler = getDefaultUseCaseHandler();
        } else if (mExecutor.equals(ExecutorFactory.background()) && mResponsePoster.equals(ExecutorFactory.ui())) {
            useCaseHandler = getDefaultBackgroundUseCaseHandler();
        } else {
            useCaseHandler = new UseCaseHandler(new UseCaseSchedulerImpl(mExecutor, mResponsePoster));
        }

        // reset mExecutor and mResponsePoster to reuse the DefaultUseCaseHandlerBuilder
        mExecutor = mResponsePoster = null;

        return useCaseHandler;
    }

    public static UseCaseHandler getDefaultUseCaseHandler() {
        if (DEFAULT_HANDLER == null) {
            DEFAULT_HANDLER = newUseCaseHandler(ExecutorFactory.immediate(), ExecutorFactory.ui());
        }
        return DEFAULT_HANDLER;
    }

    public static UseCaseHandler getDefaultBackgroundUseCaseHandler() {
        if (DEFAULT_BACKGROUND_HANDLER == null) {
            DEFAULT_BACKGROUND_HANDLER = newUseCaseHandler(ExecutorFactory.background(), ExecutorFactory.ui());
        }
        return DEFAULT_BACKGROUND_HANDLER;
    }

    public static UseCaseHandler newUseCaseHandler(Executor executor, Executor responsePoster) {
        return new UseCaseHandler(new UseCaseSchedulerImpl(executor, responsePoster));
    }

    private static class UseCaseSchedulerImpl implements UseCaseScheduler {
        /**
         * Used for posting responses, typically to the main thread.
         */
        private Executor mResponsePoster;
        /**
         * Used for execute Runnable
         */
        private Executor mExecutor;

        public UseCaseSchedulerImpl(Executor executor, Executor responsePoster) {
            mExecutor = executor;
            mResponsePoster = responsePoster;
        }

        @Override
        public void execute(Runnable runnable) {
            mExecutor.execute(runnable);
        }

        @Override
        public <V extends UseCase.ResponseValue> void notifyResponse(final V response, final UseCase.UseCaseCallback<V> useCaseCallback) {
            if (useCaseCallback == null) {
                return;
            }
            mResponsePoster.execute(new Runnable() {
                @Override
                public void run() {
                    useCaseCallback.onSuccess(response);
                }
            });
        }

        @Override
        public <V extends UseCase.ResponseValue> void onError(final Exception e, final UseCase.UseCaseCallback<V> useCaseCallback) {
            if (useCaseCallback == null) {
                return;
            }
            mResponsePoster.execute(new Runnable() {
                @Override
                public void run() {
                    useCaseCallback.onError(e);
                }
            });
        }
    }
}
