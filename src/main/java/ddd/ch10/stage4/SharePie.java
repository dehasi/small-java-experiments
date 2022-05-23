package ddd.ch10.stage4;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class SharePie {

    private Map<Company, Share> shares;

    private double getAmount() {
        double total = 0;
        Iterator<Company> it = shares.keySet().iterator();
        while (it.hasNext()) {
            Share loanShare = shares.get(it.next());
            total = total + loanShare.amount;
        }
        return total;
    }

    SharePie minus(SharePie otherShares) {
        SharePie result = new SharePie();
        Set<Company> owners = new HashSet<>();
        owners.addAll(getOwners());
        owners.addAll(otherShares.getOwners());
        Iterator<Company> it = owners.iterator();
        while (it.hasNext()) {
            Company owner = it.next();
            double resultShareAmount = getShareAmount(owner) - otherShares.getShareAmount(owner);
            result.add(owner, resultShareAmount);
        }
        return result;
    }

    private void add(Company owner, double amount) {
        shares.put(owner, new Share(owner, amount));
    }

    private double getShareAmount(Company owner) {
        return shares.get(owner).amount;
    }

    private Collection<Company> getOwners() {
        return shares.keySet();
    }

    public SharePie prorated(double amountToProrate) {

        SharePie proration = new SharePie();
        double basis = getAmount();
        Iterator<Company> it = shares.keySet().iterator();
        while (it.hasNext()) {
            Company owner = it.next();
            Share share = getShare(owner);
            double proratedShareAmount = share.amount / basis * amountToProrate;
            proration.add(owner, proratedShareAmount);
        }
        return proration;
    }

    private Share getShare(Company owner) {
        return shares.get(owner);
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
