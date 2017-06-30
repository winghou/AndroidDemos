package com.benio.demoproject.usecase;

import java.util.concurrent.Executor;

public class ImmediateExecutor implements Executor {
    public static final Executor INSTANCE = new ImmediateExecutor();

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}