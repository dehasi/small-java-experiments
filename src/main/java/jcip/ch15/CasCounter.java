package jcip.ch15;

class CasCounter {

    private final SimulatedCAS value = new SimulatedCAS(0);

    int get() {return value.get();}

    int increment() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
}
