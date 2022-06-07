package jcip.ch14;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class ConditionalBoundedBuffer<ELEMENT> {

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    @GuardedBy("this") private final ELEMENT[] buffer;
    @GuardedBy("this") private int head, tail, count;

    @SuppressWarnings("unchecked") // only ELEMENT will be put
    ConditionalBoundedBuffer(int capacity) {
        buffer = (ELEMENT[])new Object[capacity];
    }

    void put(ELEMENT element) throws InterruptedException {
        lock.lock();
        try {
            while (isFull()) notFull.await();
            doPut(element);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    ELEMENT take() throws InterruptedException {
        lock.lock();
        try {
            while (isEmpty()) notEmpty.await();
            ELEMENT element = doTake();
            notFull.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }

    private void doPut(ELEMENT element) {
        buffer[tail] = element;
        tail = (tail + 1) % buffer.length;
        ++count;
    }

    private ELEMENT doTake() {
        ELEMENT element = buffer[head];
        buffer[head] = null;
        head = (head + 1) % buffer.length;
        --count;
        return element;
    }

    private boolean isEmpty() {
        return count == 0;
    }

    private boolean isFull() {
        return count == buffer.length;
    }
}
