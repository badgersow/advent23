import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Fourteenth {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/fourteenth.in"));

        var boardList = new ArrayList<char[]>();
        while (in.hasNextLine()) {
            boardList.add(in.nextLine().toCharArray());
        }
        var board = boardList.toArray(new char[][]{});

        int n = board.length, m = board[0].length;
        // Move rocks north by bubble sorting each column.
        for (int j = 0; j < m; j++) {
            boolean swapped = false;
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
