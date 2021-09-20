package jcip.ch8;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class SequentialPuzzleSolver<POSITION, MOVE> {

    private final Puzzle<POSITION, MOVE> puzzle;
    private final Set<POSITION> seen = new HashSet<>();

    SequentialPuzzleSolver(Puzzle<POSITION, MOVE> puzzle) {
        this.puzzle = puzzle;
    }

    List<MOVE> solve() {
        POSITION position = puzzle.initialPosition();
        return search(new Node<>(position, null, null));
    }

    private List<MOVE> search(Node<POSITION, MOVE> node) {
        if (!seen.contains(node.position)) {
            seen.add(node.position);
            if(puzzle.isGoal(node.position))
                return node.asMoveList();
            for (MOVE move : puzzle.legalMoves(node.position)) {
                POSITION position = puzzle.move(node.position, move);
                Node<POSITION,MOVE> child = new Node<>(position, move, node);
                List<MOVE> result = search(child);
                if(result!=null)
                    return result;
            }
        }
        return null;
    }
}
