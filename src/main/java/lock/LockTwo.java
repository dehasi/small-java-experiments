package lock;


class LockTwo extends Lock {

    private volatile int victim;

    @Override
    public void lock() {
        int i = (int) Thread.currentThread().getId();
        victim = i;
        // let the other go first
        while (victim == i) { } // wait
    }

    @Override
    public void unlock() {
    }
}
