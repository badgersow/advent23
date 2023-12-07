import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Sixth2 {
    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/sixth.in"));

        long time = Long.parseLong(String.join("", in.nextLine().split(":\\s+")[1].split("\\s+")));
        long record = Long.parseLong(String.join("", in.nextLine().split(":\\s+")[1].split("\\s+")));

        long range = 0;
        for (long hold = 0; hold <= time; hold++) {
            long distance = (time - hold) * hold;
            if (distance > record) {
                range++;
            }
        }

        System.out.println(range);
    }
}
