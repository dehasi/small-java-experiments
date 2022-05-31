package jcip.ch11;

import jcip.annotations.ThreadSafe;

@ThreadSafe
class StripedMap {
    // synchronized policy: buckets[n] guarded by locks[n%N_LOCKS]
    static final int N_LOCKS = 16;
    final Node[] buckets;
    final Object[] locks;

    StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; ++i)
            locks[i] = new Object();
    }

    Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {
            for (Node n = buckets[hash]; n != null; n = n.next()) {
                if (n.key.equals(key)) return n.value;
            }
        }
        return null;
    }

    void clear() {
        for (int i = 0; i < buckets.length; ++i) {
            synchronized (locks[i % N_LOCKS]) {
                buckets[i] = null;
            }
        }
    }

    private int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    static class Node {
        Object key;
        Object value;

        Node next() {
            return null;
        }
    }
}
