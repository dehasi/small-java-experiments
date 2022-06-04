package jcip.ch12;

import java.util.Random;

class AccountTest {
    public static final int THRESHOLD = 10;
    Random random = new Random();

    public synchronized void transferCredits(Account from, Account to, int amount) {
        from.setBalance(from.getBalance() - amount);
        if (random.nextInt(1000) > THRESHOLD)
            Thread.yield();
        to.setBalance(to.getBalance() + amount);
    }

    static class Account {
        int balance;

        public int getBalance() {
            return balance;
        }

        public Account setBalance(int balance) {
            this.balance = balance;
            return this;
        }
    }
}
