package jcip.ch10.account;

class TransferMoney {
    private static final Object tieLock = new Object();

    static void transferMoney(final Account fromAcc, final Account toAcc, final DollarAmount amount)
        throws InsufficientFundException {
        class Helper {
            public void transfer() throws InsufficientFundException {
                if (fromAcc.balance.compareTo(amount) < 0)
                    throw new InsufficientFundException();
                else {
                    fromAcc.debit(amount);
                    toAcc.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAcc);
        int toHash = System.identityHashCode(toAcc);
        if (fromHash < toHash) {
            synchronized (fromAcc) {
                synchronized (toAcc) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcc) {
                synchronized (fromAcc) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcc) {
                    synchronized (toAcc) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }
}
