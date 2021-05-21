package jcip.ch6.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

class CancellingFuture {

    private static final long TIME_BUDGET = 42;
    private static final Ad DEFAULT_AD = new Ad();
    private ExecutorService exec = Executors.newFixedThreadPool(24);

    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAddTask());

        Page page = renderBody();
        Ad ad;
        try {
            long timeLeft = endNanos - System.nanoTime();
            ad = f.get(timeLeft, NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }
        page.setAd(ad);
        return page;
    }

    private Page renderBody() {
        return new Page();
    }

    private class Page {
        public void setAd(Ad ad) { }
    }

    private class FetchAddTask implements Callable<Ad> {
        @Override public Ad call() throws Exception {
            return null;
        }
    }

    static class Ad {}
}
