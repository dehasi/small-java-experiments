package ddd.ch10.stage3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Loan {
    private SharePie shares;

    public void applyPrincipalPaymentShares(Map<Company, Share> paymentShares) {
       getShares().decrease(paymentShares);
    }

    public Map<Company, Share> calculatePrincipalPaymentShares(double paymentAmount) {
        return getShares().prorate(paymentAmount);
    }

    private SharePie getShares() {
        return this.shares;
    }

    public static void main(String[] args) {
        Map<Company, Share> paymentShares = null;
        double paymentAmount = 13.;
        Loan aLoan = new Loan();
        aLoan.calculatePrincipalPaymentShares(paymentAmount);
        aLoan.applyPrincipalPaymentShares(paymentShares);
    }
}
