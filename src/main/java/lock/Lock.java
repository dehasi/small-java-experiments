package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

abstract class Lock implements java.util.concurrent.locks.Lock {

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
