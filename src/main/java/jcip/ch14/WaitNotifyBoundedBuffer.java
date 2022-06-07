package jcip.ch14;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class WaitNotifyBoundedBuffer<ELEMENT> {

    @GuardedBy("this") private final ELEMENT[] buffer;
    @GuardedBy("this") private int head, tail, count;

    @SuppressWarnings("unchecked") // only ELEMENT will be put
    WaitNotifyBoundedBuffer(int capacity) {
        buffer = (ELEMENT[])new Object[capacity];
    }

    synchronized void put(ELEMENT element) throws InterruptedException {
        while (isFull()) wait();

        doPut(element);
        notifyAll();
    }

    synchronized void alternatePut(ELEMENT element) throws InterruptedException {
        while (isFull()) wait();

        boolean wasEmpty = isEmpty();
        doPut(element);
        if (wasEmpty) notifyAll();
    }

    synchronized ELEMENT take() throws InterruptedException {
        while (isEmpty()) wait();

        ELEMENT element = doTake();
        notifyAll();
        return element;
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
