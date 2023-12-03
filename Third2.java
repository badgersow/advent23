import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Third2 {
    static boolean isGear(char c) {
        return c == '*';
    }

    static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/third.in"));
        var field = new ArrayList<char[]>();
        while (in.hasNextLine()) {
            field.add(in.nextLine().toCharArray());
        }

        int n = field.size();
        int m = field.get(0).length;

        var gearCount = new int[n][m];
        var gearValue = new long[n][m];
        for (var array : gearValue) {
            Arrays.fill(array, 1);
        }

        for (int i = 0; i < field.size(); i++) {
            for (int j = 0; j < n; j++) {
                // If first digit, then process. Otherwise, do not.
                if (!isDigit(field.get(i)[j])) {
                    // Is not even a digit.
                    continue;
                }
                if (j > 0 && isDigit(field.get(i)[j - 1])) {
                    // Is not a first digit.
                    continue;
                }

                // It's a first digit. Find out the number.
                // Also for each adjacent gear update its count and value.
                long number = 0;
                var gears = new HashSet<List<Integer>>();
                for (int k = j; k < n; k++) {
                    if (isDigit(field.get(i)[k])) {
                        number *= 10;
                        number += field.get(i)[k] - '0';

                        for (int di = -1; di <= 1; di++) {
                            for (int dj = -1; dj <= 1; dj++) {
                                int I = i + di;
                                int J = k + dj;

                                if (I >= 0 && I < field.size() && J > 0 && J < n) {
                                    if (isGear(field.get(I)[J])) {
                                        gears.add(List.of(I, J));
                                    }
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }

                for (var gear : gears) {
                    gearCount[gear.get(0)][gear.get(1)] += 1;
                    gearValue[gear.get(0)][gear.get(1)] *= number;
                }
            }
        }

        long result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (gearCount[i][j] == 2) {
                    result += gearValue[i][j];
                }
            }
        }
        System.out.println(result);
    }
}
