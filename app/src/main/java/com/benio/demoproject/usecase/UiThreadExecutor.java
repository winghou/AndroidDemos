package com.benio.demoproject.usecase;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by zhangzhibin on 2017/5/5.
 */
public class UiThreadExecutor implements Executor {
    public static final Executor INSTANCE = new UiThreadExecutor();

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable command) {
        // 当前线程已经是ui线程，则直接调用run()
        if (Thread.currentThread() == mHandler.getLooper().getThread()) {
            command.run();
        } else {
            mHandler.post(command);
        }
    }
}
