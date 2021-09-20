package jcip.ch7;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import jcip.annotations.GuardedBy;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

abstract class WebCrawler {

    private volatile TrackingExecutor executor;
    @GuardedBy("this") private final Set<URL> urlsToCrawl = new HashSet<>();

    synchronized void start() {
        executor = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) submitCrawTask(url);
        urlsToCrawl.clear();
    }

    synchronized void stop() throws InterruptedException {
        try {
            saveUncrawledTask(executor.shutdownNow());
            if (executor.awaitTermination(10, MICROSECONDS))
                saveUncrawledTask(executor.getCancelledTasks());
        } finally {
            executor = null;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private void saveUncrawledTask(List<Runnable> uncrawled) {
        for (Runnable task : uncrawled)
            urlsToCrawl.add(((CrawlTask)task).getPage());
    }

    private void submitCrawTask(URL url) {
        executor.execute(new CrawlTask(url));
    }

    class CrawlTask implements Runnable {

        private final URL url;

        public CrawlTask(URL url) {
            this.url = url;
        }

        @Override public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) return;
                submitCrawTask(link);
            }
        }

        URL getPage() { return url; }
    }
}
