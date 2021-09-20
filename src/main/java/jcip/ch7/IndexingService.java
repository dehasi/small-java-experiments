package jcip.ch7;

import java.io.File;
import java.util.concurrent.BlockingQueue;

class IndexingService {
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private BlockingQueue<File> queue;

    void start() { consumer.start(); producer.start();}

    void stop() { producer.interrupt(); }

    void awaitTermination() throws InterruptedException {
        consumer.join();
    }
    class IndexerThread extends Thread {}

    class CrawlerThread extends Thread {}
}
