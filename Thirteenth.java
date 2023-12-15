import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Thirteenth {
    // Number of columns below the vertical line.
    // Zero if none.
    static long solve(char[][] arr) {
        int n = arr.length; // height
        int m = arr[0].length; // width
        // Candidate is on the left of character.
        c:
        for (int candidate = 1; candidate < m; candidate++) {
            for (int delta = 0; candidate - delta - 1 >= 0 && candidate + delta < m; delta++) {
                for (int i = 0; i < n; i++) {
                    if (arr[i][candidate - delta - 1] != arr[i][candidate + delta]) {
                        continue c;
                    }
                }
            }
            return candidate;
        }
        return 0;
    }

    static char[][] transpose(char[][] arr) {
        char[][] result = new char[arr[0].length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                result[j][i] = arr[i][j];
            }
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/thirteenth.in"));
        long result = 0;
        while (true) {
            if (!in.hasNextLine()) {
                break;
            }
            var field = new ArrayList<char[]>();
            while (true) {
                if (!in.hasNextLine()) {
                    break;
                }
                var line = in.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                field.add(line.toCharArray());
            }

            char[][] arr = field.toArray(new char[][]{});
            long ver = solve(arr);
            long hor = solve(transpose(arr));
            if (ver == 0 && hor == 0) {
                throw new IllegalStateException("Can't find mirror");
            }
            result += ver + 100 * hor;
        }
        System.out.println(result);
    }
}
