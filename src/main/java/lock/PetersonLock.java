package lock;

class PetersonLock extends Lock{

    // thread-local index, 0 or 1
    private boolean[] flag = new boolean[2];
    private volatile int victim;

    @Override
    public void lock() {
        int i = (int) Thread.currentThread().getId();
        int j = 1 - i;
        flag[i] = true; // I'm interested
        victim = i; // you go first
        while (flag[j] && victim == 1); // wait
    }

    @Override
    public void unlock() {
        int i = (int) Thread.currentThread().getId();
        flag[i] = false; // I'm not interested
    }
}
