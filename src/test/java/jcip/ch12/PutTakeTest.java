package jcip.ch12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;

class PutTakeTest {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTraits, nPairs;

    public static void main(String[] args) {
        new PutTakeTest(10, 10, 1000).test();
        pool.shutdown();
    }

    PutTakeTest(int capacity, int nTraits, int nPairs) {
        this.bb = new BoundedBuffer<>(capacity);
        this.nTraits = nTraits;
        this.nPairs = nPairs;
        this.barrier = new CyclicBarrier(2 * nPairs + 1);
    }

    void test() {
        try {
            for (int i = 0; i < nPairs; ++i) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await(); // wait for all threads to be ready
            barrier.await(); // wait for all threads to finish
            Assertions.assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable {
        @Override public void run() {
            try {
                int seed = (this.hashCode() ^ (int)System.nanoTime());
                int sum = 0;
                barrier.await();
                for (int i = nTraits; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {
        @Override public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = nTraits; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
