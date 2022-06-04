package jcip.ch13;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

class TimedLocking {

    Lock lock = new ReentrantLock();

    boolean trySendSharedLine(String message, long timeout, TimeUnit unit) throws InterruptedException {
        long nanosToLock = unit.toNanos(timeout) - estimatedNanosToSend(message);

        if (lock.tryLock(nanosToLock, NANOSECONDS)) return false;

        try {
            return sendSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    private boolean sendSharedLine(String message) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return cancellableSendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
        return false;
    }

    private long estimatedNanosToSend(String message) {
        return 42;
    }
}
