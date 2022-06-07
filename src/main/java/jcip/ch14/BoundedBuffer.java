package jcip.ch14;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class BoundedBuffer<ELEMENT> {

    @GuardedBy("this") private final ELEMENT[] buffer;
    @GuardedBy("this") private int head, tail, count;

    @SuppressWarnings("unchecked") // only ELEMENT will be put
    BoundedBuffer(int capacity) {
        buffer = (ELEMENT[])new Object[capacity];
    }

    synchronized void toPut(ELEMENT element) {
        buffer[tail] = element;
        tail = (tail + 1) % buffer.length;
        ++count;
    }

    synchronized ELEMENT toTake() {
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
