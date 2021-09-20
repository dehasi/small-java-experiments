package jcip.ch4;

import jcip.annotations.Immutable;

@Immutable
class Point {
    final int x,y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
