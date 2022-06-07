package jcip.ch14;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class ThreadGate {
    @GuardedBy("this") private boolean isOpen;
    @GuardedBy("this") private int generation;

    synchronized void close() {
        isOpen = false;
    }

    synchronized void open() {
        ++generation;
        isOpen = true;
        notifyAll();
    }

    synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation)
            wait();
    }

}
