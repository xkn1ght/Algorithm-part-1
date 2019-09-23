import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * @author knight
 */

public class Solver {
    private final int SIZE;
    private MinPQ<SearchNode> priorityQueue;
    private LinkedList<Board> gameTree;

    public enum METHODS{
        A_SEARCH,
        CLIMBING,
        WIDE_SEARCH,
        DEEP_SEARCH,
    }

    public Solver(Board initial) {
        SIZE = initial.dimension();
        SearchNode processed = new SearchNode(initial);

        if (!processed.presentBoard.isSolvable(SIZE)) {
            System.out.println("This initial board is unsolvable! now refine it");
            processed.presentBoard.twin(SIZE);
        }

        priorityQueue = new MinPQ<>(Comparator.comparingInt(SearchNode::getPriority));
        priorityQueue.insert(processed);

        gameTree = new LinkedList<>();
        gameTree.add(initial);
        int i = 0;

        while (!isSolvable()) {
            System.out.println("steps: " + i++);
            processed = priorityQueue.delMin();
            LinkedList<Board> neighbors = (LinkedList<Board>) processed.validatedNeighbors();

            gameTree.add(processed.presentBoard);
            System.out.println("\nthe game tree" + processed.presentBoard + processed.steps);
            for (Board board : neighbors) {
                int step = processed.steps + 1;
                priorityQueue.insert(new SearchNode(board, step, processed));
            }
        }

        System.out.println("Successful!");
        for (Board board : gameTree) {
            System.out.println(board + "||||||||");
        }
    }

    public boolean isSolvable() {
        return priorityQueue.min().presentBoard.isGoal();
    }

    public int moves() {
        return priorityQueue.min().steps;
    }

    public Iterable<Board> solution() {
        return gameTree;
    }

    private class SearchNode {
        private Board presentBoard;
        private int steps;
        private SearchNode previousNode;

        private SearchNode(Board board, int steps, SearchNode previous) {
            if (board == null){
                throw new IllegalArgumentException();
            }
            presentBoard = board;
            this.steps = steps;
            previousNode = previous;
        }

        private SearchNode(Board board) {
            this(board, 0, null);
        }

        private int getPriority() {
            return presentBoard.hamming() + steps;
        }

        private Iterable<Board> validatedNeighbors() {
            LinkedList<Board> boards = (LinkedList<Board>) presentBoard.neighbors();
            Board toBeDel = null;
            for (Board b : boards) {
                if (previousNode == null) {
                    break;
                }
                if (previousNode.presentBoard.equals(b)) {
                    toBeDel = b;
                    break;
                }
            }
            boards.remove(toBeDel);

            return boards;
        }

    }
    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

//             solve the slider puzzle
            Board initial = new Board(tiles);
            Solver solver = new Solver(initial);
            StdOut.println(filename + ": " + solver.moves());
        }
    }

}
