package com.benio.demoproject.usecase;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhibin on 2017/5/5.
 */
public class ExecutorFactory {
    // Allows for simultaneous reads and writes.
    private static final int NUM_IO_BOUND_THREADS = 2;

    private static Executor sIoBoundExecutor;

    public static final int POOL_SIZE = 2;

    public static final int MAX_POOL_SIZE = 4;

    public static final int TIMEOUT = 30;

    private static Executor sBackgroundExecutor;

    public static Executor immediate() {
        return ImmediateExecutor.INSTANCE;
    }

    public static Executor ui() {
        return UiThreadExecutor.INSTANCE;
    }

    public static Executor io() {
        if (sIoBoundExecutor == null) {
            sIoBoundExecutor = Executors.newFixedThreadPool(NUM_IO_BOUND_THREADS);
        }
        return sIoBoundExecutor;
    }

    public static Executor background() {
        if (sBackgroundExecutor == null) {
            sBackgroundExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                    TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
        }
        return sBackgroundExecutor;
    }
}
