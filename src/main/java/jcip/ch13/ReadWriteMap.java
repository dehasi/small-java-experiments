package jcip.ch13;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWriteMap<KEY, VALUE> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    private final Map<KEY, VALUE> map;

    ReadWriteMap(Map<KEY, VALUE> map) {this.map = map;}

    VALUE put(KEY key, VALUE value) {
        write.lock();
        try {
            return map.put(key, value);
        } finally {
            write.unlock();
        }
    }

    VALUE get(KEY key) {
        read.lock();
        try {
            return map.get(key);
        } finally {
            read.unlock();
        }
    }
}
