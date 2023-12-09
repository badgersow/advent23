import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Ninth2 {
    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/ninth.in"));

        long result = 0;
        while (in.hasNextLine()) {
            List<Long> initial = Arrays.stream(in.nextLine().split(" ")).map(Long::parseLong).toList();
            long[][] ext = new long[initial.size() + 1][initial.size() + 1];
            for (int i = 0; i < initial.size(); i++) {
                ext[0][i + 1] = initial.get(i);
            }
            // Calculate diffs.
            for (int i = 1; i < ext.length; i++) {
                for (int j = 1; j < ext.length - 1 - i; j++) {
                    ext[i][j] = ext[i - 1][j + 1] - ext[i - 1][j];
                }
            }
            // Extrapolate.
            for (int i = ext.length - 2; i >= 0; i--) {
                for (int j = ext.length - i - 3; j >= 0; j--) {
                    ext[i][j] = ext[i][j + 1] - ext[i + 1][j];
                }
            }

            result += ext[0][0];
        }
        System.out.println(result);
    }
}
