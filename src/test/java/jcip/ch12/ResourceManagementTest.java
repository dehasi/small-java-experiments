package jcip.ch12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceManagementTest {

    private static final int CAPACITY = 42;
    private static final double THRESHOLD = .000001;

    @Test void noLeaks() throws InterruptedException {
        BoundedBuffer<Big> bb = new BoundedBuffer<>(CAPACITY);
        int heapSize1 = snapshotHeap();

        for (int i = 0; i < CAPACITY; ++i)
            bb.put(new Big());
        for (int i = 0; i < CAPACITY; ++i)
            bb.take();

        int heapSize2 = snapshotHeap();
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }

    private int snapshotHeap() {
        return 42; // in reality use the special library
    }

    static class Big {
        double[] data = new double[100_000];
    }
}
