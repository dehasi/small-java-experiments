package jcip.ch6.executors;

import java.util.concurrent.Executor;

class WithinThreadTaskExecutor implements Executor {
    @Override public void execute(Runnable command) {
        command.run();
    }
}
