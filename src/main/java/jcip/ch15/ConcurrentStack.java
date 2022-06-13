package jcip.ch15;

import java.util.concurrent.atomic.AtomicReference;

class ConcurrentStack<E> {
    final AtomicReference<Node<E>> top = new AtomicReference<>();

    void push(E item) {
        Node<E> newHead = new Node<>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null) return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));

        return oldHead.item;
    }

    private static class Node<E> {
        final E item;
        Node<E> next;

        private Node(E item) {this.item = item;}
    }
}
