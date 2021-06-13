package jcip.ch5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class CellularAutomata {

    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, mainBoard::commitNewValues);
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++)
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));

    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        @Override public void run() {
            while (board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++)
                    for (int y = 0; y < board.getMaxY(); y++)
                        board.setNewValue(x, y, computeValue(x, y));
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    return;
                } catch (BrokenBarrierException e) {
                    return;
                }
            }
        }

        private Object computeValue(int x, int y) {
            return null;
        }
    }

    static class Board {
        void commitNewValues() {}

        public Board getSubBoard(int count, int i) {
            return null;
        }

        public boolean hasConverged() {
            return false;
        }

        public int getMaxX() {
            return 0;
        }

        public int getMaxY() {
            return 0;
        }

        public void setNewValue(int x, int y, Object value) { }
    }
}

