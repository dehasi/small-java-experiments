package jcip.ch6.executors;

import java.util.concurrent.Executor;

class ThreadPerTaskExecutor implements Executor {
    @Override public void execute(Runnable command) {
        new Thread(command).start();
    }
}
