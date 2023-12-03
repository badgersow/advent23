import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Third {
    static boolean isSymbol(char c) {
        return !isDigit(c) && c != '.';
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

        long result = 0;
        // For each first digit, find the number.
        // Add this number if there is an adjacent symbol.
        for (int i = 0; i < field.size(); i++) {
            int n = field.get(i).length;
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
                long number = 0;
                boolean hasAdjacentSymbol = false;
                for (int k = j; k < n; k++) {
                    if (isDigit(field.get(i)[k])) {
                        number *= 10;
                        number += field.get(i)[k] - '0';

                        for (int di = -1; di <= 1; di++) {
                            for (int dj = -1; dj <= 1; dj++) {
                                int I = i + di;
                                int J = k + dj;

                                if (I >= 0 && I < field.size() && J > 0 && J < n) {
                                    if (isSymbol(field.get(I)[J])) {
                                        hasAdjacentSymbol = true;
                                    }
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }

                if (hasAdjacentSymbol) {
                    result += number;
                }
            }
        }

        System.out.println(result);
    }
}
