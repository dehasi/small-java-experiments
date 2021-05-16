package ej3e.ch11.item78;

import java.util.concurrent.TimeUnit;

class StopThread3 {

    private static volatile boolean stopRequested;

    public static void main(String[] args) throws InterruptedException {
        Thread backgroundThread = new Thread(() -> {
            int i = 0;
            while (!stopRequested)
                ++i;
        });

        backgroundThread.start();

        TimeUnit.SECONDS.sleep(1);
        stopRequested = true;
        backgroundThread.join();
    }

}
