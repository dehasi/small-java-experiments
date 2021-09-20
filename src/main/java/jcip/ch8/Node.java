package jcip.ch8;

import java.util.LinkedList;
import java.util.List;
import jcip.annotations.Immutable;

@Immutable
class Node<POSITION, MOVE> {

    final POSITION position;
    final MOVE move;
    final Node<POSITION, MOVE> prev;

    Node(POSITION position, MOVE move, Node<POSITION, MOVE> prev) {
        this.position = position;
        this.move = move;
        this.prev = prev;
    }

    List<MOVE> asMoveList() {
        List<MOVE> solution = new LinkedList<>();
        for (Node<POSITION, MOVE> n = this; n.move != null; n = n.prev)
            solution.add(0, n.move);
        return solution;
    }
}
