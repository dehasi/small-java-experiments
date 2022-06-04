package jcip.ch13;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class DeadlockAvoidance {

    private static Random rnd = new Random();

    public boolean transferMoney(Account fromAcct, Account toAcct, DollarAmount amount, long timeout, TimeUnit unit)
        throws InsufficientFundException, InterruptedException {

        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {
            if (fromAcct.lock.tryLock()) {
                try {
                    if (toAcct.lock.tryLock()) {
                        try {
                            if (fromAcct.balance.compareTo(amount) < 0)
                                throw new InsufficientFundException();

                            fromAcct.debit(amount);
                            toAcct.credit(amount);
                            return true;
                        } finally {
                            toAcct.lock.unlock();
                        }
                    }
                } finally {
                    fromAcct.lock.unlock();
                }
            }
            if (System.nanoTime() > stopTime) return false;
            TimeUnit.NANOSECONDS.sleep(fixedDelay + rnd.nextInt() % randMod);
        }
    }

    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;

    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }

    static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }

    private static class Account {

        Lock lock = new ReentrantLock();
        DollarAmount balance = new DollarAmount(42);

        private void debit(DollarAmount amount) {}

        private void credit(DollarAmount amount) {}
    }

    private static record DollarAmount(int amount) implements Comparable<DollarAmount> {

        @Override public int compareTo(DeadlockAvoidance.DollarAmount that) {
            return Integer.compare(this.amount, that.amount);
        }
    }

    private static class InsufficientFundException extends RuntimeException {}
}
