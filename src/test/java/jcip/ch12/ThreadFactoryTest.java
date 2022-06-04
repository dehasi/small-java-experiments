package jcip.ch12;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadFactoryTest {

    private final TestingThreadFactory threadFactory = new TestingThreadFactory();

    @Test void poolExpansion() throws InterruptedException {
        int MAX_SIZE = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_SIZE, threadFactory);

        for (int i = 0; i < 10 * MAX_SIZE; ++i)
            executorService.execute(() -> {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        for (int i = 0; i < 20 && threadFactory.numCreated.get() < MAX_SIZE; ++i)
            Thread.sleep(100);

        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
        executorService.shutdown();
    }

    static class TestingThreadFactory implements ThreadFactory {

        final AtomicInteger numCreated = new AtomicInteger();
        private final ThreadFactory factory = Executors.defaultThreadFactory();

        @Override public Thread newThread(Runnable runnable) {
            numCreated.incrementAndGet();
            return factory.newThread(runnable);
        }
    }
}
