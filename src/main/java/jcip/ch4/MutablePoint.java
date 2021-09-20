package jcip.ch4;

import jcip.annotations.NotThreadSafe;

@NotThreadSafe
class MutablePoint {
    int x, y;

    public MutablePoint() {
        x = y = 0;
    }

    public MutablePoint(MutablePoint p) {
        this.x = p.x;
        this.y = p.y;
    }
}
