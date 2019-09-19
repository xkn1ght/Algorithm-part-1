import java.util.LinkedList;

public class Board {
    private int[][] tiles;
    private final int size;
    private int blankRow;
    private int blankCol;

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    public Board(int[][] tiles) {
        this.tiles = tiles.clone();
        size = tiles.length;
        updateField();
    }

    private void updateField() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                s.append(tiles[i][j]).append("  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public int dimension() {
        return size;
    }

    public int hamming() {
        int wrong = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int index = i * size + j + 1;
                if (tiles[i][j] == 0 && index != size * size) {
                    wrong++;
                } else if (tiles[i][j] != 0 && tiles[i][j] != index) {
                    wrong++;
                }
            }
        }
        return wrong;
    }

    public int manhattan() {
        int wrong = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int index = i * size + j + 1;
                if (i == blankRow && j == blankCol)
                    wrong += 9 - index;
                wrong += Math.abs(index - tiles[i][j]);

            }
        }

        return wrong;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Board that = (Board) o;
        if (size != that.size) return false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (that.tiles[i][j] != this.tiles[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        LinkedList<Board> boards = new LinkedList<>();


        if (blankCol > 0) {
            Board board = new Board(copyOf(tiles));
            board.swap(Direction.LEFT);
            board.updateField();
            boards.add(board);
        }
        if (blankCol < size - 1) {
            Board board = new Board(copyOf(tiles));
            board.swap(Direction.RIGHT);
            board.updateField();
            boards.add(board);
        }
        if (blankRow > 0) {
            Board board = new Board(copyOf(tiles));
            board.swap(Direction.UP);
            board.updateField();
            boards.add(board);
        }
        if (blankRow < size - 1) {
            Board board = new Board(copyOf(tiles));
            board.swap(Direction.DOWN);
            board.updateField();
            boards.add(board);
        }

        return boards;

    }

    public void swap(Direction direction) {
        int temp = tiles[blankRow][blankCol];
        switch (direction) {
            case UP:
                tiles[blankRow][blankCol] = tiles[blankRow - 1][blankCol];
                tiles[blankRow - 1][blankCol] = temp;
                break;
            case DOWN:
                tiles[blankRow][blankCol] = tiles[blankRow + 1][blankCol];
                tiles[blankRow + 1][blankCol] = temp;
                break;
            case LEFT:
                tiles[blankRow][blankCol] = tiles[blankRow][blankCol - 1];
                tiles[blankRow][blankCol - 1] = temp;
                break;
            case RIGHT:
                tiles[blankRow][blankCol] = tiles[blankRow][blankCol + 1];
                tiles[blankRow][blankCol + 1] = temp;
        }
        updateField();
    }

    public void twin(int size) {
        int[][] newTiles = copyOf(this.tiles);
        do {
            if (size == 2) {
                swap(newTiles, 0, 0, 1, 1);
            } else if (size % 2 == 0) {
                swap(newTiles, 0, 0, 0, 2);
            } else {
                swap(newTiles, 0, 0, 0, 2);
            }
            tiles = newTiles;
            updateField();
        } while (!isSolvable(size));
    }

    private void swap(int[][] a, int i, int j, int k, int l) {
        int temp = a[i][j];
        a[i][j] = a[k][l];
        a[k][l] = temp;
    }

    private int[][] copyOf(int[][] a) {
        int[][] result = new int[a.length][];
        int length = a.length;
        for (int i = 0; i < length; i++) {
            result[i] = a[i].clone();
        }
        return result;
    }

    private int reverseNumber() {
        int reverse = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == blankRow && j == blankCol)
                    continue;
                for (int k = i * size + j + 1; k < size * size; k++) {
                    if (k != blankRow * size + blankCol) {
                        int x = k / size;
                        int y = k % size;
                        if (tiles[i][j] > tiles[x][y]) {
                            reverse++;
                        }
                    }
                }
            }
        }
        return reverse;
    }

    public boolean isSolvable(int size) {
        if (size < 2)
            throw new IllegalArgumentException();
        if (size % 2 == 1) {
            return reverseNumber() % 2 == 0;
        } else {
            return reverseNumber() % 2 == 1 ^ blankRow % 2 == 1;
        }
    }
}