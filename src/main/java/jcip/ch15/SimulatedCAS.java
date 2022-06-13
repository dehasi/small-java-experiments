package jcip.ch15;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class SimulatedCAS {
    @GuardedBy("this") private int value;

    SimulatedCAS(int value) {
        this.value = value;
    }

    synchronized int get() {
        return value;
    }

    synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (value == expectedValue)
            value = newValue;

        return oldValue;
    }

    synchronized boolean compareAndSet(int expectedValue, int newValue) {
       return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}
