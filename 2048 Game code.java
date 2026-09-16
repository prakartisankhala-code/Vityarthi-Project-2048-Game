import java.util.*;

public class Game2048 {

    static final int SIZE = 4;
    static int[][] board = new int[SIZE][SIZE];
    static int score = 0;

    // ANSI Colors
    static final String RESET = "\u001B[0m";
    static final String BOLD = "\u001B[1m";
    static final String CYAN = "\u001B[36m";
    static final String YELLOW = "\u001B[33m";
    static final String GREEN = "\u001B[32m";
    static final String RED = "\u001B[31m";
    static final String WHITE = "\u001B[37m";
    static final String BLUE = "\u001B[34m";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        addRandomTile();
        addRandomTile();

        while (true) {

            clearScreen();
            printHeader();
            printBoard();

            System.out.println();
            System.out.println(CYAN + "   SCORE: " + YELLOW + score + RESET);
            System.out.println();

            System.out.println(BOLD + "   CONTROLS" + RESET);
            System.out.println("   " + GREEN + "[W]" + RESET + " Up");
            System.out.println("   " + GREEN + "[S]" + RESET + " Down");
            System.out.println("   " + GREEN + "[A]" + RESET + " Left");
            System.out.println("   " + GREEN + "[D]" + RESET + " Right");
            System.out.println("   " + RED + "[Q]" + RESET + " Quit");

            System.out.print("\n   Enter your move: ");

            char move = sc.next().toUpperCase().charAt(0);

            if (move == 'Q') {
                clearScreen();
                System.out.println();
                System.out.println(CYAN + BOLD);
                System.out.println("        ╔══════════════════════════╗");
                System.out.println("        ║      GAME EXITED!       ║");
                System.out.println("        ╚══════════════════════════╝");
                System.out.println(RESET);
                System.out.println("        Final Score: " + YELLOW + score + RESET);
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
                    System.out.println(RED + "\n   Invalid input! Use W, A, S, D or Q." + RESET);
                    pause();
            }

            if (moved) {
                addRandomTile();
            }

            if (hasWon()) {

                clearScreen();
                printHeader();
                printBoard();

                System.out.println();
                System.out.println(GREEN + BOLD);
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║     🎉 YOU REACHED 2048!    ║");
                System.out.println("   ╚══════════════════════════════╝");
                System.out.println(RESET);

                System.out.println("   Your Score: " + YELLOW + score + RESET);
                break;
            }

            if (!canMove()) {

                clearScreen();
                printHeader();
                printBoard();

                System.out.println();
                System.out.println(RED + BOLD);
                System.out.println("   ╔══════════════════════════════╗");
                System.out.println("   ║          GAME OVER!         ║");
                System.out.println("   ╚══════════════════════════════╝");
                System.out.println(RESET);

                System.out.println("   Final Score: " + YELLOW + score + RESET);
                break;
            }
        }

        sc.close();
    }

    // =========================
    // HEADER
    // =========================

    static void printHeader() {

        System.out.println(CYAN + BOLD);
        System.out.println("        ██████╗  ██████╗ ██╗  ██╗");
        System.out.println("        ╚════██╗██╔═████╗╚██╗██╔╝");
        System.out.println("         █████╔╝██║██╔██║ ╚███╔╝ ");
        System.out.println("        ██╔═══╝ ████╔╝██║ ██╔██╗ ");
        System.out.println("        ███████╗╚██████╔╝██╔╝ ██╗");
        System.out.println("        ╚══════╝ ╚═════╝ ╚═╝  ╚═╝");
        System.out.println(RESET);

        System.out.println(YELLOW + "              JAVA EDITION" + RESET);
        System.out.println();
    }

    // =========================
    // PRINT BOARD
    // =========================

    static void printBoard() {

        String line = "   +------+------+------+------+\n";

        System.out.print(CYAN + line + RESET);

        for (int i = 0; i < SIZE; i++) {

            System.out.print(CYAN + "   |" + RESET);

            for (int j = 0; j < SIZE; j++) {

                int value = board[i][j];

                if (value == 0) {

                    System.out.print("      " + CYAN + "|" + RESET);

                } else {

                    String tile = getTileColor(value);

                    System.out.print(tile + String.format("%4d  ", value)
                            + RESET + CYAN + "|" + RESET);
                }
            }

            System.out.println();
            System.out.print(CYAN + line + RESET);
        }
    }

    // =========================
    // TILE COLORS
    // =========================

    static String getTileColor(int value) {

        switch (value) {

            case 2:
                return "\u001B[37m";

            case 4:
                return "\u001B[36m";

            case 8:
                return "\u001B[32m";

            case 16:
                return "\u001B[33m";

            case 32:
                return "\u001B[35m";

            case 64:
                return "\u001B[31m";

            case 128:
                return "\u001B[34m";

            case 256:
                return "\u001B[36m";

            case 512:
                return "\u001B[32m";

            case 1024:
                return "\u001B[33m";

            case 2048:
                return "\u001B[31m" + BOLD;

            default:
                return WHITE;
        }
    }

    // =========================
    // ADD RANDOM TILE
    // =========================

    static void addRandomTile() {

        ArrayList<int[]> emptyCells = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == 0) {

                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return;
        }

        Random random = new Random();

        int[] cell =
                emptyCells.get(random.nextInt(emptyCells.size()));

        board[cell[0]][cell[1]] =
                random.nextInt(10) == 0 ? 4 : 2;
    }

    // =========================
    // MOVE LEFT
    // =========================

    static boolean moveLeft() {

        boolean moved = false;

        for (int i = 0; i < SIZE; i++) {

            int[] row = new int[SIZE];

            int index = 0;

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] != 0) {

                    row[index++] = board[i][j];
                }
            }

            for (int j = 0; j < SIZE - 1; j++) {

                if (row[j] != 0 &&
                        row[j] == row[j + 1]) {

                    row[j] *= 2;

                    score += row[j];

                    row[j + 1] = 0;
                }
            }

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

    // =========================
    // MOVE RIGHT
    // =========================

    static boolean moveRight() {

        reverseRows();

        boolean moved = moveLeft();

        reverseRows();

        return moved;
    }

    // =========================
    // MOVE UP
    // =========================

    static boolean moveUp() {

        transpose();

        boolean moved = moveLeft();

        transpose();

        return moved;
    }

    // =========================
    // MOVE DOWN
    // =========================

    static boolean moveDown() {

        transpose();

        reverseRows();

        boolean moved = moveLeft();

        reverseRows();

        transpose();

        return moved;
    }

    // =========================
    // REVERSE ROWS
    // =========================

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

    // =========================
    // TRANSPOSE
    // =========================

    static void transpose() {

        for (int i = 0; i < SIZE; i++) {

            for (int j = i + 1; j < SIZE; j++) {

                int temp = board[i][j];

                board[i][j] = board[j][i];

                board[j][i] = temp;
            }
        }
    }

    // =========================
    // CHECK WIN
    // =========================

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

    // =========================
    // CHECK MOVES
    // =========================

    static boolean canMove() {

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == 0) {

                    return true;
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE - 1; j++) {

                if (board[i][j] == board[i][j + 1]) {

                    return true;
                }
            }
        }

        for (int i = 0; i < SIZE - 1; i++) {

            for (int j = 0; j < SIZE; j++) {

                if (board[i][j] == board[i + 1][j]) {

                    return true;
                }
            }
        }

        return false;
    }

    // =========================
    // CLEAR SCREEN
    // =========================

    static void clearScreen() {

        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // =========================
    // PAUSE
    // =========================

    static void pause() {

        Scanner temp = new Scanner(System.in);

        System.out.println("\nPress ENTER to continue...");
        temp.nextLine();
    }
}