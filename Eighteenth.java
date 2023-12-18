import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Eighteenth {

    static Vec DOWN = new Vec(1, 0);
    static Vec UP = new Vec(-1, 0);
    static Vec LEFT = new Vec(0, -1);
    static Vec RIGHT = new Vec(0, 1);

    static int SIDE = 10_000;

    static int OFFSET = SIDE / 2;

    static class Vec {
        int i, j;

        public Vec(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Vec add(Vec other) {
            return new Vec(i + other.i, j + other.j);
        }

        public Vec offset() {
            return new Vec(i + OFFSET, j + OFFSET);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/eighteenth.in"));

        // 1 means "dug"
        int[][] field = new int[SIDE][SIDE];

        var dirByChar = Map.of('D', DOWN, 'U', UP, 'L', LEFT, 'R', RIGHT);
        // R 6 (#70c710)
        // D 5 (#0dc571)

        var here = new Vec(0, 0).offset();
        field[here.i][here.j] = 1;
        while (in.hasNextLine()) {
            var line = in.nextLine();
            var dirChar = line.split(" ")[0].charAt(0);
            var length = Integer.parseInt(line.split(" ")[1]);

            for (int k = 1; k <= length; k++) {
                here = here.add(dirByChar.get(dirChar));
                field[here.i][here.j] = 1;
            }
        }

        fill(field);

        // Count the number of zeros and ones.
        int holes = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] != -1) {
                    holes++;
                }
            }
        }

        System.out.println(holes);
    }

    static void fill(int[][] field) {
        int n = field.length;
        int m = field[0].length;

        var stack = new ArrayDeque<Vec>();
        stack.add(new Vec(0, 0));
        while (!stack.isEmpty()) {
            var here = stack.removeFirst();
            field[here.i][here.j] = -1;
            for (var dir : List.of(UP, DOWN, LEFT, RIGHT)) {
                var there = here.add(dir);
                if (there.i >= 0 && there.i < n &&
                        there.j >= 0 && there.j < m &&
                        field[there.i][there.j] == 0) {
                    stack.addFirst(there);
                }
            }
        }
    }
}
