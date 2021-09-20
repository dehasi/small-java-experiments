package jcip.ch4;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class Counter {
    @GuardedBy("this") private long value = 0;

    synchronized long getValue() {
        return value;
    }

    synchronized long increment() {
        if (value == Long.MAX_VALUE)
            throw new IllegalStateException("counter overflow");
        return ++value;
    }
}
