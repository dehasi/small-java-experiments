package jcip.ch12;

import java.util.concurrent.Semaphore;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class BoundedBuffer<E> {
    private final Semaphore availableItems, availableSpaces;
    @GuardedBy("this") private final E[] items;
    @GuardedBy("this") private int putPosition = 0, takePosition = 0;

    @SuppressWarnings("unchecked") // the array will keep only E
    BoundedBuffer(int capacity) {
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[])new Object[capacity];
    }

    void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length) ? 0 : i;
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length) ? 0 : i;
        return x;
    }

    boolean isEmpty() {return availableItems.availablePermits() == 0;}

    boolean isFull() {return availableSpaces.availablePermits() == 0;}
}
