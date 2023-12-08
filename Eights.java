import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Eights {

    static Map<String, String> left = new HashMap<>();

    static Map<String, String> right = new HashMap<>();

    static Map<Position, Integer> dp = new HashMap<>();

    static char[] string;

    static class Position {

        public Position(int index, String node) {
            this.index = index;
            this.node = node;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position1 = (Position) o;
            return index == position1.index && Objects.equals(node, position1.node);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, node);
        }

        int index;
        String node;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/current.in"));

        string = in.nextLine().toCharArray();
        in.nextLine();
        while (in.hasNextLine()) {
            var line = in.nextLine();
            var node = line.split(" = ")[0];
            var pathLeft = line.split(" = ")[1].split(", ")[0].substring(1);
            var pathRight = line.split(" = ")[1].split(", ")[1];
            pathRight = pathRight.substring(0, pathRight.length() - 1);

            left.put(node, pathLeft);
            right.put(node, pathRight);
        }

        int result = 0;
        int position = 0;
        String node = "AAA";
        while (true) {
            if (node.equals("ZZZ")) {
                break;
            }

            result++;
            if (string[position] == 'L') {
                node = left.get(node);
                position = (position + 1) % string.length;
            } else {
                node = right.get(node);
                position = (position + 1) % string.length;
            }
        }

        System.out.println(result);
    }
}
