package jcip.ch15;

abstract class PseudoRandom {

    protected int calculateNext(int i) {return i + 1;}
    abstract int nextInt(int n);
}
