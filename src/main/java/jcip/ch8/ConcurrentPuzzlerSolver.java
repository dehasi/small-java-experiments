package jcip.ch8;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

class ConcurrentPuzzlerSolver<POSITION, MOVE> {

    private final Puzzle<POSITION, MOVE> puzzle;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<POSITION, Boolean> seen;
    final ValueLatch<Node<POSITION, MOVE>> solution = new ValueLatch<>();

    ConcurrentPuzzlerSolver(Puzzle<POSITION, MOVE> puzzle, ExecutorService executorService) {
        this.puzzle = puzzle;
        this.executorService = executorService;
        this.seen = new ConcurrentHashMap<>();
    }

    List<MOVE> solve() throws InterruptedException {
        try {
            POSITION position = puzzle.initialPosition();
            executorService.execute(newTask(position, null, null));
            Node<POSITION, MOVE> solutionValue = solution.getValue();
            return solutionValue == null ? null : solutionValue.asMoveList();
        } finally {
            executorService.shutdown();
        }
    }

    private Runnable newTask(POSITION position, MOVE move, Node<POSITION, MOVE> node) {
        return new SolverTask(position, move, node);
    }

    @ThreadSafe
    static class ValueLatch<T> {
        @GuardedBy("this") private T value = null;
        private final CountDownLatch done = new CountDownLatch(1);

        public boolean isSet() {
            return (done.getCount() == 0);
        }

        synchronized void setValue(T value) {
            if (!isSet()) {
                this.value = value;
                done.countDown();
            }
        }

        T getValue() throws InterruptedException {
            done.await();
            synchronized (this) {
                return value;
            }
        }
    }

    private class SolverTask extends Node<POSITION, MOVE> implements Runnable {

        public SolverTask(POSITION position, MOVE move, Node<POSITION, MOVE> node) {
            super(position, move, node);
        }

        @Override public void run() {
            if (solution.isSet() || seen.putIfAbsent(position, true) != null)
                return; // already solved or seen this position
            if (puzzle.isGoal(position))
                solution.setValue(this);
            else
                for (MOVE move : puzzle.legalMoves(position))
                    executorService.execute(newTask(puzzle.move(position, move), move, this));
        }
    }
}
