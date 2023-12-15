import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Fifteenth {

    static int hash(String s) {
        int result = 0;
        for (char c : s.toCharArray()) {
            result += c;
            result *= 17;
            result %= 256;
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/fifteenth.in"));

        var strings = in.nextLine().split(",");
        long result = 0;
        for (var s : strings) {
            result += hash(s);
        }
        System.out.println(result);
    }
}
