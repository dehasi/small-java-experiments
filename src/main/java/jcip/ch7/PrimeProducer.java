package jcip.ch7;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

class PrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {this.queue = queue;}

    @Override public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException e) {
            /* Allow thread to exit */
        }
    }

    public void cancel() {
        interrupt();
    }
}
