package jcip.ch12;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;

class TimedPutTakeTest {
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final BarrierTimer timer;
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTraits, nPairs;

    public static void main(String[] args) throws Exception {
        int tpt = 100_000; // trails per thread
        for (int cap = 1; cap <= 1000; cap *= 10) {
            System.out.println("Capacity: " + cap);
            for (int pairs = 1; pairs <= 128; pairs *= 2) {
                TimedPutTakeTest t = new TimedPutTakeTest(cap, tpt, pairs);
                System.out.print("Pairs: " + pairs + "\t");
                t.test();
                System.out.print("\t");
                Thread.sleep(1_000);
                t.test();
                System.out.println();
                Thread.sleep(1_000);
            }
        }
        pool.shutdown();
    }

    TimedPutTakeTest(int capacity, int nTraits, int nPairs) {
        this.bb = new BoundedBuffer<>(capacity);
        this.nTraits = nTraits;
        this.nPairs = nPairs;
        this.timer = new BarrierTimer();
        this.barrier = new CyclicBarrier(2 * nPairs + 1, timer);
    }

    void test() {
        try {
            timer.clear();
            for (int i = 0; i < nPairs; ++i) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await(); // wait for all threads to be ready
            barrier.await(); // wait for all threads to finish
            long nsPerItem = timer.time() / (nPairs * (long)nTraits);
            System.out.print("Throughput: " + nsPerItem + " ns/item");
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

    static class BarrierTimer implements Runnable {
        private boolean started;
        private long startTime, endTime;

        @Override public synchronized void run() {
            long t = System.nanoTime();
            if (!started) {
                started = true;
                startTime = t;
            } else
                endTime = t;
        }

        synchronized void clear() {started = false;}

        synchronized long time() {return endTime - startTime;}
    }
}
