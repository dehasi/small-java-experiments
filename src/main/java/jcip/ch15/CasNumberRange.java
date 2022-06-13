package jcip.ch15;

import java.util.concurrent.atomic.AtomicReference;
import jcip.annotations.Immutable;

class CasNumberRange {

    @Immutable
    record IntPair(int lower, int upper) {}

    private final AtomicReference<IntPair> values = new AtomicReference<>(new IntPair(0, 0));

    int getLower() {return values.get().lower();}

    int getUpper() {return values.get().upper();}

    void setLower(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i > oldv.lower()) throw new IllegalArgumentException("Can't set lower to " + i + "> upper " + oldv.upper());

            IntPair newv = new IntPair(i, oldv.upper());
            if (values.compareAndSet(oldv, newv)) return;
        }
    }
}
