package ddd.ch10.stage1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Loan {
    private Map<Company, Share> shares;

    public Map<Company, Share> distributedPrincipalPayment(double paymentAmount) {
        Map<Company, Share> paymentShares = new HashMap<>();
        Map<Company, Share> loanShares = getShares();
        double total = getAmount();
        Iterator<Company> it = loanShares.keySet().iterator();
        while (it.hasNext()) {
            Company owner = it.next();
            double initialLoanShareAmount = getShareAmount(owner);
            double paymentsShareAmount = initialLoanShareAmount / total * paymentAmount;
            Share paymentShare = new Share(owner, paymentsShareAmount);
            paymentShares.put(owner, paymentShare);

            double newLoanShareAmount = initialLoanShareAmount - paymentsShareAmount;
            Share newLoanShare = new Share(owner, newLoanShareAmount);
            loanShares.put(owner, newLoanShare);
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

    private double getShareAmount(Company owner) {
        return 0;
    }

    private Map<Company, Share> getShares() {
        return this.shares;
    }
}
