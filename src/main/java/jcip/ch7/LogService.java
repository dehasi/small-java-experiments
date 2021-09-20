package jcip.ch7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import jcip.annotations.GuardedBy;

class LogService {

    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this") private boolean isShutdown;
    @GuardedBy("this") private int reservations;

    LogService(Writer writer) {
        this.queue = new LinkedBlockingQueue<>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    void start() {
        loggerThread.start();
    }

    void stop() {
        synchronized (this) { isShutdown = true; }
        loggerThread.interrupt();
    }

    void log(String msg) throws InterruptedException {
        synchronized (this) {
            if (isShutdown) throw new IllegalStateException("Logger is shut down");
            ++reservations;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        @Override public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            if (isShutdown && reservations == 0) break;
                        }
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }
                        writer.println(msg);
                    } catch (InterruptedException retry) { }
                }
            } finally {
                writer.close();
            }
        }
    }
}
