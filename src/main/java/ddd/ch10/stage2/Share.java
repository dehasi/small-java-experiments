package ddd.ch10.stage2;

class Share {
    Company owner;
    double amount;

    public Share(Company owner, double amount) {
        this.owner = owner;
        this.amount = amount;
    }
}
