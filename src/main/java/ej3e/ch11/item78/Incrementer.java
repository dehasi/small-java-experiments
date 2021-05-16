package ej3e.ch11.item78;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Incrementer {
    volatile int val = 0;

    int incrementAndGet() { // wrong
        return ++val;
    }

    public static void main(String[] args) throws InterruptedException {
        new Incrementer().run();

    }

    void run() throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        int n = 1000;
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                incrementAndGet();
            }));
        }
        for (int i = 0; i < n; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < n; i++) {
            threads.get(i).join();
        }

        System.out.println(val); // 982
    }
}
