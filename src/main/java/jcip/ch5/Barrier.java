package jcip.ch5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Barrier {

    CyclicBarrier barrier = new CyclicBarrier(2, () -> System.out.println("barrier"));

    public static void main(String[] args) {
        new Barrier().run();
    }

    private void run() {

        try {
            barrier.await(1L, SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}