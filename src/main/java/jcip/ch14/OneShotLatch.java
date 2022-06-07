package jcip.ch14;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

class OneShotLatch {

    private final Sync sync = new Sync();

    void signal() {sync.releaseShared(0);}

    void await() throws InterruptedException {sync.acquireSharedInterruptibly(0);}

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override protected int tryAcquireShared(int ignored) {
            return getState() == 1 ? 1 : -1;
        }

        @Override protected boolean tryReleaseShared(int ignored) {
            setState(1);
            return true;
        }
    }
}
