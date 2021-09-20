package jcip.ch4;

import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class SafePoint {
    @GuardedBy("this") private int x, y;

    private SafePoint(int[] a) {this(a[0], a[1]);}

    SafePoint(SafePoint p) {
        this(p.get());
    }

    SafePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    synchronized int[] get() {
        return new int[] {x, y};
    }

    synchronized void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
