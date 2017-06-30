package com.benio.demoproject.usecase;

import java.util.concurrent.Executor;

/**
 * Created by zhangzhibin on 2017/5/5.
 */
public class DefaultUseCaseHandlerBuilder implements UseCaseHandlerBuilder {
    private static UseCaseHandlerBuilder sInstance;

    private UseCaseHandler mUseCaseHandler;

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

        // reset scheduler
        UseCaseSchedulerImpl scheduler = UseCaseSchedulerImpl.INSTANCE;
        scheduler.init(mExecutor, mResponsePoster);

        if (mUseCaseHandler == null) {
            mUseCaseHandler = new UseCaseHandler(scheduler);
        }

        // reset mExecutor and mResponsePoster to reuse the DefaultUseCaseHandlerBuilder
        mExecutor = mResponsePoster = null;

        return mUseCaseHandler;
    }

    private static class UseCaseSchedulerImpl implements UseCaseScheduler {
        public static final UseCaseSchedulerImpl INSTANCE = new UseCaseSchedulerImpl();
        /**
         * Used for posting responses, typically to the main thread.
         */
        private Executor mResponsePoster;
        /**
         * Used for execute Runnable
         */
        private Executor mExecutor;

        public void init(Executor executor, Executor responsePoster) {
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
