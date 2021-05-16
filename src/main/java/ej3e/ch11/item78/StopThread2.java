package ej3e.ch11.item78;

import java.util.concurrent.TimeUnit;

class StopThread2 {

    private static boolean stopRequested;

    static synchronized boolean stopRequested() {
        return stopRequested;
    }

    static synchronized void stop() {
        stopRequested = true;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested())
                ++i;
        });

        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stop();
        backgroundThread.join();
    }

}
