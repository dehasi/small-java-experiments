package jcip.ch7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

import static java.util.concurrent.TimeUnit.SECONDS;

@ThreadSafe
class QueuePrimeGenerator extends Thread {

    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();
    private volatile boolean cancelled;

    private final BlockingQueue<BigInteger> queue;

    QueuePrimeGenerator(BlockingQueue<BigInteger> queue) {this.queue = queue;}

    @Override public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {
            // Allow thread to exit
        }
    }

    public void cancel() {
        interrupt();
    }

    public synchronized List<BigInteger> get() {
        return primes;
    }

    List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        QueuePrimeGenerator generator = new QueuePrimeGenerator(queue);
        new Thread(generator).start();
        try {
            SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }
        return generator.get();
    }
}
