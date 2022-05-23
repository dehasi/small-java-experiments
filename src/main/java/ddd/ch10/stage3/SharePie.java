package ddd.ch10.stage3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class SharePie {

    private Map<Company, Share> shares;

    public Map<Company, Share> prorate(double paymentAmount) {
        Map<Company, Share> paymentShares = new HashMap<>();
        Map<Company, Share> loanShares = getShares();
        double total = getAmount();
        Iterator<Company> it = loanShares.keySet().iterator();
        while (it.hasNext()) {
            Company lender = it.next();
            Share loanShare = loanShares.get(lender);
            double paymentsShareAmount = loanShare.amount / total * paymentAmount;
            Share paymentShare = new Share(lender, paymentsShareAmount);
            paymentShares.put(lender, paymentShare);
        }
        return paymentShares;
    }

    private double getAmount() {
        Map<Company, Share> loanShares = getShares();
        double total = .0;
        Iterator<Company> it = loanShares.keySet().iterator();
        while (it.hasNext()) {
            Share loanShare = loanShares.get(it.next());
            total = total + loanShare.amount;
        }
        return total;
    }

    private Map<Company, Share> getShares() {
        return this.shares;
    }

    public void decrease(Map<Company, Share> paymentShares) {
        Map<Company, Share> loanShares = getShares();
        Iterator<Company> it = paymentShares.keySet().iterator();
        while (it.hasNext()) {
            Company lender = it.next();
            Share paymentShare = paymentShares.get(lender);
            Share loanShare = loanShares.get(lender);
            double newLoanShareAmount = loanShare.amount - paymentShare.amount;
            Share newLoanShare = new Share(lender, newLoanShareAmount);
            loanShares.put(lender, newLoanShare);
        }
    }
}
