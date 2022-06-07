package jcip.ch14;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class SleepyBoundedBuffer<ELEMENT> {

    private static final int SLEEP_GRANULARITY = 10;

    @GuardedBy("this") private final ELEMENT[] buffer;
    @GuardedBy("this") private int head, tail, count;

    @SuppressWarnings("unchecked") // only ELEMENT will be put
    SleepyBoundedBuffer(int capacity) {
        buffer = (ELEMENT[])new Object[capacity];
    }

    void put(ELEMENT element) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(element);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    ELEMENT take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty()) {
                    return doTake();
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    synchronized void doPut(ELEMENT element) {
        buffer[tail] = element;
        tail = (tail + 1) % buffer.length;
        ++count;
    }

    synchronized ELEMENT doTake() {
        ELEMENT element = buffer[head];
        buffer[head] = null;
        head = (head + 1) % buffer.length;
        --count;
        return element;
    }

    synchronized boolean isEmpty() {
        return count == 0;
    }

    synchronized boolean isFull() {
        return count == buffer.length;
    }
}
