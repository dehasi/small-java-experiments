package ddd.ch10.stage4;

class Loan {
    private SharePie shares;

    public void applyPrincipalPaymentShares(SharePie paymentShares) {
        shares = shares.minus(paymentShares);
    }

    public SharePie calculatePrincipalPaymentShares(double paymentAmount) {
        return getShares().prorated(paymentAmount);
    }

    private SharePie getShares() {
        return this.shares;
    }

    public static void main(String[] args) {
        SharePie paymentShares = null;
        double paymentAmount = 13.;
        Loan aLoan = new Loan();
        aLoan.calculatePrincipalPaymentShares(paymentAmount);
        aLoan.applyPrincipalPaymentShares(paymentShares);
    }
}
