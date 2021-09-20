package jcip.ch8;

import java.util.Set;

public interface Puzzle<POSITION, MOVE> {

    POSITION initialPosition();

    boolean isGoal(POSITION position);

    Set<MOVE> legalMoves(POSITION position);

    POSITION move(POSITION position, MOVE move);
}
