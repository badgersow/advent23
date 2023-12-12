import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Twelfth {

    static char[] row;

    static int[] groups;

    static long[][] dp;

    static long solve(int pos, int group) {
        if (pos == row.length && group == groups.length) {
            // One way to do nothing.
            return 1;
        }
        if (pos == row.length) {
            // There are groups left, but we ran out of characters.
            return 0;
        }
        if (dp[pos][group] >= 0) {
            return dp[pos][group];
        }

        // What we can do?
        // 1. Consume entire group if possible, OR
        // 2. Skip a character, if possible.

        long result = 0;
        // Consume group, if there are any groups left.
        if (group < groups.length) {
            int size = groups[group];
            // Do we have "size" continuous # or ? next to current position?
            int suitableChars = 0;
            for (int i = pos; i < Math.min(pos + size, row.length); i++) {
                if (row[i] == '#' || row[i] == '?') {
                    suitableChars++;
                } else {
                    break;
                }
            }
            // Can skip if:
            // There are necessary number of broken springs.
            if (suitableChars == size) {
                // It's end of string, or there is an empty spring after that.
                if (pos + size == row.length) {
                    result += solve(pos + size, group + 1);
                } else if (row[pos + size] == '.' || row[pos + size] == '?') {
                    result += solve(pos + size + 1, group + 1);
                }
            }
        }

        // Skip character.
        // Can only do this, if the character is . or ?
        if (row[pos] == '.' || row[pos] == '?') {
            result += solve(pos + 1, group);
        }

        return dp[pos][group] = result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/twelfth.in"));

        long sum = 0;
        while (in.hasNextLine()) {
            row = in.next().toCharArray();
            groups = Arrays.stream(in.next().split(",")).mapToInt(Integer::parseInt).toArray();
            dp = new long[row.length][groups.length + 1];
            for (long[] dpRow : dp) {
                Arrays.fill(dpRow, -1);
            }

            long options = solve(0, 0);
            sum += options;
        }

        System.out.println("Total: " + sum);
    }
}
