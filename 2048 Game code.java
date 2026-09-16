import java.util.*;

public class Game2048 {

    static final int SIZE = 4;
    static int[][] board = new int[SIZE][SIZE];
    static int score = 0;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        addRandomTile();
        addRandomTile();

        while (true) {

            printBoard();

            System.out.println("Score: " + score);
            System.out.println("Use W = Up, S = Down, A = Left, D = Right");
            System.out.println("Enter Q to quit:");

            char move = sc.next().toUpperCase().charAt(0);

            if (move == 'Q') {
                System.out.println("Game exited.");
                break;
            }

            boolean moved = false;

            switch (move) {
                case 'W':
                    moved = moveUp();
                    break;

                case 'S':
                    moved = moveDown();
                    break;

                case 'A':
                    moved = moveLeft();
                    break;

                case 'D':
                    moved = moveRight();
                    break;

                default:
                    System.out.println("Invalid input!");
            }

            if (moved) {
                addRandomTile();
            }

            if (hasWon()) {
                printBoard();
                System.out.println("🎉 Congratulations! You reached 2048!");
                break;
            }

            if (!canMove()) {
                printBoard();
                System.out.println("Game Over!");
                System.out.println("Final Score: " + score);
                break;
            }
        }

        sc.close();
    }

    // Print the game board
    static void printBoard() {

        System.out.println("\n-------------------------");

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == 0)
                    System.out.printf("| %4s ", "-");
                else
                    System.out.printf("| %4d ", board[i][j]);
            }

            System.out.println("|");
            System.out.println("-------------------------");
        }
    }

    // Add a random tile (2 or 4)
    static void addRandomTile() {

        ArrayList<int[]> emptyCells = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty())
            return;

        Random random = new Random();

        int[] cell = emptyCells.get(
                random.nextInt(emptyCells.size())
        );

        board[cell[0]][cell[1]] =
                random.nextInt(10) == 0 ? 4 : 2;
    }

    // Move left
    static boolean moveLeft() {

        boolean moved = false;

        for (int i = 0; i < SIZE; i++) {

            int[] row = new int[SIZE];

            int index = 0;

            // Remove empty cells
            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] != 0) {
                    row[index++] = board[i][j];
                }
            }

            // Merge equal tiles
            for (int j = 0; j < SIZE - 1; j++) {

                if (row[j] != 0 &&
                        row[j] == row[j + 1]) {

                    row[j] *= 2;
                    score += row[j];

                    row[j + 1] = 0;
                }
            }

            // Compress again
            int[] newRow = new int[SIZE];
            index = 0;

            for (int value : row) {

                if (value != 0) {
                    newRow[index++] = value;
                }
            }

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] != newRow[j]) {
                    moved = true;
                }

                board[i][j] = newRow[j];
            }
        }

        return moved;
    }

    // Move right
    static boolean moveRight() {

        reverseRows();

        boolean moved = moveLeft();

        reverseRows();

        return moved;
    }

    // Move up
    static boolean moveUp() {

        transpose();

        boolean moved = moveLeft();

        transpose();

        return moved;
    }

    // Move down
    static boolean moveDown() {

        transpose();

        reverseRows();

        boolean moved = moveLeft();

        reverseRows();

        transpose();

        return moved;
    }

    // Reverse every row
    static void reverseRows() {

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE / 2; j++) {

                int temp = board[i][j];

                board[i][j] =
                        board[i][SIZE - 1 - j];

                board[i][SIZE - 1 - j] = temp;
            }
        }
    }

    // Transpose the board
    static void transpose() {

        for (int i = 0; i < SIZE; i++) {

            for (int j = i + 1; j < SIZE; j++) {

                int temp = board[i][j];

                board[i][j] = board[j][i];

                board[j][i] = temp;
            }
        }
    }

    // Check whether 2048 has been reached
    static boolean hasWon() {

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == 2048) {
                    return true;
                }
            }
        }

        return false;
    }

    // Check whether any move is possible
    static boolean canMove() {

        // Check empty cells
        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == 0) {
                    return true;
                }
            }
        }

        // Check horizontal combinations
        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE - 1; j++) {

                if (board[i][j] == board[i][j + 1]) {
                    return true;
                }
            }
        }

        // Check vertical combinations
        for (int i = 0; i < SIZE - 1; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == board[i + 1][j]) {
                    return true;
                }
            }
        }

        return false;
    }
}