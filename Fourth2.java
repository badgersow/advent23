import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Fourth2 {

    static int points(String card) {
        var winning = new HashSet<Long>();
        for (String num : card.split(":\\s+")[1].split("\\s+\\|\\s+")[0].split("\\s+")) {
            winning.add(Long.parseLong(num));
        }
        int count = 0;
        for (String num : card.split(":\\s+")[1].split("\\s+\\|\\s+")[1].split("\\s+")) {
            if (winning.contains(Long.parseLong(num))) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/fourth.in"));
        long result = 0;
        var lines = new ArrayList<String>();
        while (in.hasNextLine()) {
            lines.add(in.nextLine());
        }
        int n = lines.size();
        long[] copies = new long[n];
        Arrays.fill(copies, 1);
        for (int i = 0; i < n; i++) {
            int p = points(lines.get(i));
            for (int j = 1; j <= p; j++) {
                copies[i + j] += copies[i];
            }
        }

        System.out.println(Arrays.stream(copies).sum());
    }
}
