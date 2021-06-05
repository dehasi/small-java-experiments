package jcip.ch5;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class FileCrawler implements Runnable {

    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    @Override public void run() {
        try {
            crawl(root);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles(fileFilter);
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory())
                    crawl(entry);
                else if (!alreadyIndexed(entry))
                    fileQueue.put(entry);
            }
        }
    }

    private boolean alreadyIndexed(File entry) {
        return false;
    }

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(100);
        FileFilter filter = path -> true;

        for (File root : roots)
            new Thread(new FileCrawler(queue, filter, root)).start();
        for (int i = 0; i < 10; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }
}

class Indexer implements Runnable {

    private final BlockingQueue<File> queue;

    Indexer(BlockingQueue<File> queue) {this.queue = queue;}

    @Override public void run() {
        try {
            while (true)
                indexFile(queue.take());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File take) {}
}
