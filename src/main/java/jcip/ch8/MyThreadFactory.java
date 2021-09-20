package jcip.ch8;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;

class MyThreadFactory implements ThreadFactory {

    private final String poolName;

    MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override public Thread newThread(Runnable task) {
        return new MyThread(task, poolName);
    }

    private static class MyThread extends Thread {

        private static final String DEFAULT_NAME = "MyAppThread";
        private static volatile boolean debugLifecycle = false;
        private static final AtomicInteger created = new AtomicInteger();
        private static final AtomicInteger alive = new AtomicInteger();
        private static final Logger log = Logger.getAnonymousLogger();

        public MyThread(Runnable target) {
            this(target, DEFAULT_NAME);
        }

        public MyThread(Runnable target, String name) {
            super(target, name + "-" + created.incrementAndGet());
            setUncaughtExceptionHandler((t, e) -> log.log(SEVERE, "UNCAUGHT in thread " + t.getName(), e));
        }

        @Override public void run() {
            // Copy debug flag to ensure consistent value throughout
            boolean debug = debugLifecycle;
            if (debug) log.log(FINE, "Created " + getName());
            try {
                alive.incrementAndGet();
                super.run();
            } finally {
                alive.decrementAndGet();
                if (debug) log.log(FINE, "Exiting " + getName());
            }
        }

        public static int getThreadsCreated() { return created.get(); }

        public static int getThreadsAlive() { return alive.get(); }

        public static boolean getDebug() { return debugLifecycle; }

        public static void setDebug(boolean debug) { debugLifecycle = debug; }
    }
}
