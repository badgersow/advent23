import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sixteenth2 {

    static char[][] arr;
    static int[][] visited;
    static int n, m;

    static class Vec {
        int i, j;

        public Vec(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Vec add(Vec other) {
            return new Vec(i + other.i, j + other.j);
        }

        public List<Vec> mirror(char m) {
            if (m == '.') {
                return List.of(this);
            }
            if (m == '/') {
                return List.of(new Vec(-j, -i));
            }
            if (m == '\\') {
                return List.of(new Vec(j, i));
            }
            if (m == '-') {
                if (i == 0) {
                    return List.of(this);
                }
                return List.of(new Vec(0, -1), new Vec(0, 1));
            }
            if (m == '|') {
                if (j == 0) {
                    return List.of(this);
                }
                return List.of(new Vec(1, 0), new Vec(-1, 0));
            }
            throw new IllegalStateException("Unknown mirror character: " + m);
        }

        public int bit() {
            if (i == -1) {
                return 1;
            }
            if (j == -1) {
                return 1 << 1;
            }
            if (i == 1) {
                return 1 << 2;
            }
            if (j == 1) {
                return 1 << 3;
            }
            throw new IllegalStateException("Unknown bit for vector " + this);
        }

        @Override
        public String toString() {
            return "(%d, %d)".formatted(i, j);
        }
    }

    static int energized() {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (visited[i][j] > 0) {
                    result++;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/sixteenth.in"));

        var fieldList = new ArrayList<char[]>();
        while (in.hasNextLine()) {
            fieldList.add(in.nextLine().toCharArray());
        }

        arr = fieldList.toArray(new char[][]{});
        n = arr.length;
        m = arr[0].length;
        // Horizontal

        int best = -1;
        for (int i = 0; i < n; i++) {
            // From left
            visited = new int[n][m];
            trace(i, 0, new Vec(0, 1));
            best = Math.max(best, energized());

            // From right
            visited = new int[n][m];
            trace(i, m - 1, new Vec(0, -1));
            best = Math.max(best, energized());
        }

        for (int j = 0; j < m; j++) {
            // From up
            visited = new int[n][m];
            trace(0, j, new Vec(1, 0));
            best = Math.max(best, energized());

            // From down
            visited = new int[n][m];
            trace(n - 1, j, new Vec(-1, 0));
            best = Math.max(best, energized());
        }

        System.out.println(best);
    }

    static void trace(int i, int j, Vec dir) {
        visited[i][j] |= dir.bit();

        var here = new Vec(i, j);
        var newDirs = dir.mirror(arr[i][j]);
        for (var newDir : newDirs) {
            var there = here.add(newDir);
            if (there.i < 0 || there.i >= n || there.j < 0 || there.j >= m) {
                // Out of bounds.
                continue;
            }
            if ((visited[there.i][there.j] & newDir.bit()) != 0) {
                // Already traced.
                continue;
            }

            trace(there.i, there.j, newDir);
        }
    }
}
