package jcip.ch4;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

class ListHelper<E> {
    List<E> list = synchronizedList(new ArrayList<>());

    boolean putIfAbsent(E x) {
        synchronized (list){
            boolean absent = !list.contains(x);
            if (absent) list.add(x);
            return absent;
        }
    }
}
