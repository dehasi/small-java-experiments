package lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantDeadLock {

    final java.util.concurrent.locks.Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        new ReentrantDeadLock().run();
    }

    private void run() {
        if (lock.tryLock())
            System.out.println("true1");
        if (lock.tryLock())
            System.out.println("true2");
        System.out.println("end");
    }
}
