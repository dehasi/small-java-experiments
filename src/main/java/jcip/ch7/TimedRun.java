package jcip.ch7;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jcip.util.Util.launderThrowable;

class TimedRun {
    static ExecutorService taskExec = Executors.newSingleThreadExecutor();

    static void timedRun(Runnable r, long timeout, TimeUnit unit) {
        Future<?> task = taskExec.submit(r);

        try {
            task.get(timeout, unit);
        } catch (InterruptedException | TimeoutException e) {
            // task will be cancelled below
        } catch (ExecutionException e) {
            throw launderThrowable(e);
        } finally {
            // Harmless if task is already completed
            task.cancel(/*interrupt if running*/true);
        }
    }
}
