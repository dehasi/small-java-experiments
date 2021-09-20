package jcip.ch4;

import jcip.annotations.GuardedBy;

class PrivateLock {

    private final Object myLock = new Object();
    @GuardedBy("myLock") Widget widget;

    void someMethod() {
        synchronized (myLock) {
            // Access or modify the state of widget
        }
    }

    private class Widget {}
}
