package jcip.ch6.executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

class LifecycleWebServer {

    private static final int THREADS = 100;
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!executorService.isShutdown()) {
            try {
                Socket connection = socket.accept();
                executorService.submit(() -> handleRequest(connection));
            } catch (RejectedExecutionException e) {
                if (!executorService.isShutdown())
                    log("task rejected", e);
            }
        }
    }

    private static void log(String rejected, RejectedExecutionException e) { }

    private static void handleRequest(Socket connection) {
        HttpRequest req = readRequest(connection);
        if (isShutdown(req)) {
            stop();
        } else {
            // dispatchRequest(req);
        }
    }

    private static void stop() {
        executorService.shutdown();
    }

    private static boolean isShutdown(HttpRequest req) {
        return new Random().nextBoolean();
    }

    private static HttpRequest readRequest(Socket connection) {
        return null;
    }
}
