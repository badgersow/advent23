import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Sixth {
    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/current.in"));

        List<Integer> times = Arrays.stream(in.nextLine().split(":\\s+")[1].split("\\s+")).map(Integer::parseInt).toList();
        List<Integer> records = Arrays.stream(in.nextLine().split(":\\s+")[1].split("\\s+")).map(Integer::parseInt).toList();

        long result = 1L;
        for (int i = 0; i < times.size(); i++) {
            int time = times.get(i);
            int record = records.get(i);

            int range = 0;
            for (int hold = 0; hold <= time; hold++) {
                int distance = (time - hold) * hold;
                if (distance > record) {
                    range++;
                }
            }

            result *= range;
        }

        System.out.println(result);
    }
}
