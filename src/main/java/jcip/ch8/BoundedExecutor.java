package jcip.ch8;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

class BoundedExecutor {

    private final Executor executor;
    private final Semaphore semaphore;

    BoundedExecutor(Executor executor, int bound) {
        this.executor = executor;
        this.semaphore = new Semaphore(bound);
    }

    void submitTask(final Runnable task) throws InterruptedException {
        semaphore.acquire();
        try {
            executor.execute(() -> {
                try {
                    task.run();
                } finally {
                    semaphore.release();
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }
}
