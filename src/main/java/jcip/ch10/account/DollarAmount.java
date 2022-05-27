package jcip.ch10.account;

record DollarAmount(Integer amount) implements Comparable<DollarAmount> {
    @Override public int compareTo(DollarAmount that) {
        return this.amount.compareTo(that.amount);
    }
}
