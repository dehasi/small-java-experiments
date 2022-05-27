package jcip.ch10.account;

import java.util.Random;

import static jcip.ch10.account.TransferMoney.transferMoney;

class DemonstrateDeadlock {

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1_000_000;

    public static void main(String[] args) {
        final var rnd = new Random();
        final var accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; ++i)
            accounts[i] = new Account();

        class TransferThread extends Thread {
            @Override public void run() {
                for (int i = 0; i < NUM_ITERATIONS; ++i) {
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount dollarAmount = new DollarAmount(rnd.nextInt(1000));
                    transferMoney(accounts[fromAcct], accounts[toAcct], dollarAmount);
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; ++i)
            new TransferThread().start();
    }
}
