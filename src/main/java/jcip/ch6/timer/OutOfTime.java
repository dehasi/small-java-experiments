package jcip.ch6.timer;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

class OutOfTime {

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1/*millis*/);
        SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1); // throws IllegalStateException: Timer already cancelled.
        SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask {
        @Override public void run() {
            throw new RuntimeException();
        }
    }
}
