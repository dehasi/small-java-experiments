package jcip.ch5;

import java.util.List;

class CompoundActions {

    public static <E> E getLast(List<E> list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public static <E> void deleteLast(List<E> list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }
}
