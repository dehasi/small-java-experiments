package jcip.ch7;

import java.util.concurrent.BlockingQueue;

class NoncancelableTask {

    Task getNext(BlockingQueue<Task> queue) {
        boolean interrupted = false;

        try {
            while (true)
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                    // fall through and retry
                }
        } finally {
            if (interrupted)
                Thread.currentThread().interrupt();
        }
    }

    static class Task {}
}
