package lock;

class LockOne extends Lock {
    // thread-local index, 0 or 1
    private boolean[] flag = new boolean[2];

    @Override
    public void lock() {
        int i = (int) Thread.currentThread().getId();
        int j = 1 - i;
        flag[i] = true;
        // here can be anything. we can end up with flag [true, true]
        while (flag[j]) {
        } // wait
    }

    @Override
    public void unlock() {
        int i = (int) Thread.currentThread().getId();
        flag[i] = false;
    }
}
