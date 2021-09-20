package jcip.ch7;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class CheckMail {

    boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        AtomicBoolean hasEmail = new AtomicBoolean(false);

        try {
            for (String host : hosts) {
                executorService.submit(() -> {
                    if (checkMail(host)) hasEmail.set(true);
                });
            }
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(timeout, unit);
        }
        return hasEmail.get();
    }

    private boolean checkMail(String host) {
        return false;
    }
}
