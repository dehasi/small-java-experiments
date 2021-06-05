package jcip.ch5;

import java.util.concurrent.CountDownLatch;

class TestHarness {

    long timeTasks(int nThreads, Runnable task) throws InterruptedException {
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        long startTime = System.currentTimeMillis();
        startGate.countDown();
        endGate.await();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
