import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Fourth {
    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/fourth.in"));
        long result = 0;
        while (in.hasNextLine()) {
            var line = in.nextLine();
            var winning = new HashSet<Long>();
            for (String num : line.split(":\\s+")[1].split("\\s+\\|\\s+")[0].split("\\s+")) {
                winning.add(Long.parseLong(num));
            }
            int count = 0;
            for (String num : line.split(":\\s+")[1].split("\\s+\\|\\s+")[1].split("\\s+")) {
                if (winning.contains(Long.parseLong(num))) {
                    count++;
                }
            }
            if (count > 0) {
                result += (1L << (count - 1));
            }
        }
        System.out.println(result);
    }
}
