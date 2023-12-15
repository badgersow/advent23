import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Fourteenth2 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/fourteenth.in"));

        var boardList = new ArrayList<char[]>();
        while (in.hasNextLine()) {
            boardList.add(in.nextLine().toCharArray());
        }
        var board = boardList.toArray(new char[][]{});

        int n = board.length, m = board[0].length;
        int cycles = 1_000_000_000;
        int targetCycle = -1;
        var cycleByState = new HashMap<String, Integer>();
        for (int cycle = 1; cycle <= cycles; cycle++) {
            // North
            for (int j = 0; j < m; j++) {
                bubble:
                while (true) {
                    for (int i = 1; i < n; i++) {
                        if (board[i - 1][j] == '.' && board[i][j] == 'O') {
                            board[i - 1][j] = 'O';
                            board[i][j] = '.';
                            continue bubble;
                        }
                    }
                    break;
                }
            }
            // West
            for (int i = 0; i < n; i++) {
                bubble:
                while (true) {
                    for (int j = 1; j < n; j++) {
                        if (board[i][j - 1] == '.' && board[i][j] == 'O') {
                            board[i][j - 1] = 'O';
                            board[i][j] = '.';
                            continue bubble;
                        }
                    }
                    break;
                }
            }
            // North
            for (int j = 0; j < m; j++) {
                bubble:
                while (true) {
                    for (int i = 1; i < n; i++) {
                        if (board[i - 1][j] == 'O' && board[i][j] == '.') {
                            board[i - 1][j] = '.';
                            board[i][j] = 'O';
                            continue bubble;
                        }
                    }
                    break;
                }
            }
            // East
            for (int i = 0; i < n; i++) {
                bubble:
                while (true) {
                    for (int j = 1; j < n; j++) {
                        if (board[i][j - 1] == 'O' && board[i][j] == '.') {
                            board[i][j - 1] = '.';
                            board[i][j] = 'O';
                            continue bubble;
                        }
                    }
                    break;
                }
            }

            if (targetCycle == cycle) {
                System.out.println("Reached target cycle of " + cycle);
                break;
            }
            if (targetCycle == -1) {
                var encoding = new StringBuilder();
                for (char[] row : board) {
                    encoding.append(row);
                    encoding.append('\n');
                }
                String current = encoding.toString();
                Integer prev = cycleByState.get(current);
                if (prev != null) {
                    System.out.printf("Cycle %d loops to cycle %d%n", cycle, prev);
                    // Cycle 9 loops to cycle 2
                    // 1: 1
                    // 2: 2
                    // 3: 3
                    // 4: 4
                    // 5: 5
                    // 6: 6
                    // 7: 7
                    // 8: 8
                    // 9: 2
                    // 10: 3
                    // 11: 4
                    // 12: 5
                    // 13: 6
                    // 14: 7
                    // 15: 8
                    // 16: 2
                    // 17: 3
                    // 18: 4
                    // 19: 5
                    // 20: 6
                    int toTarget = (cycles - cycle);
                    int canonical = (toTarget % (cycle - prev) + prev);
                    targetCycle = (cycle + (canonical - prev));
                    System.out.println("Result will be in cycle: " + targetCycle);
                }
                cycleByState.put(current, cycle);
            }
        }

        long result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    result += (n - i);
                }
            }
        }

        System.out.println(result);
    }
}
