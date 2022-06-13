package jcip.ch15;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReentrantLockPseudoRandom extends PseudoRandom {
    private final Lock lock = new ReentrantLock();
    private int seed;

    private ReentrantLockPseudoRandom(int seed) {
        this.seed = seed;
    }

    @Override int nextInt(int n) {
        lock.lock();
        try {
            int s = seed;
            seed = calculateNext(s);
            int remainder = s % n;
            return remainder > 0 ? remainder : remainder + n;
        } finally {
            lock.unlock();
        }
    }
}
