package jcip.ch7;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

class ExecutorLogService {

    private final ExecutorService executorService = newSingleThreadExecutor();
    private final PrintWriter writer;

    ExecutorLogService(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    void stop() throws InterruptedException {
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } finally {
            writer.close();
        }
    }

    void log(String msg) {
        try {
            executorService.submit(() -> writer.println(msg));
        } catch (RejectedExecutionException ignore) {}
    }
}
