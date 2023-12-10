import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;

public class Tenth2 {

    static class Vecs {
        static Vecs fromChar(char c) {
            if (c == '.') {
                return new Vecs(Collections.emptyList());
            }
            if (c == 'F') {
                return new Vecs(List.of(
                        new Vec(1, 0),
                        new Vec(0, 1)
                ));
            }
            if (c == 'L') {
                return new Vecs(List.of(
                        new Vec(-1, 0),
                        new Vec(0, 1)
                ));
            }
            if (c == '7') {
                return new Vecs(List.of(
                        new Vec(1, 0),
                        new Vec(0, -1)
                ));
            }
            if (c == 'J') {
                return new Vecs(List.of(
                        new Vec(-1, 0),
                        new Vec(0, -1)
                ));
            }
            if (c == '-') {
                return new Vecs(List.of(
                        new Vec(0, 1),
                        new Vec(0, -1)
                ));
            }
            if (c == '|') {
                return new Vecs(List.of(
                        new Vec(1, 0),
                        new Vec(-1, 0)
                ));
            }
            throw new IllegalArgumentException("Illegal character!");
        }

        public Vecs(List<Vec> vectors) {
            this.vectors = new HashSet<>(vectors);
        }

        public Vecs() {
        }

        Set<Vec> vectors = new HashSet<>();

        @Override
        public String toString() {
            return vectors.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vecs vecs = (Vecs) o;
            return Objects.equals(vectors, vecs.vectors);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vectors);
        }
    }

    static class Vec {

        static List<Vec> adjacent() {
            return List.of(
                    new Vec(-1, 0),
                    new Vec(1, 0),
                    new Vec(0, 1),
                    new Vec(0, -1)
            );
        }

        Vec(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Vec add(Vec other) {
            return new Vec(row + other.row, col + other.col);
        }

        Vec inv() {
            return new Vec(-row, -col);
        }

        int row;
        int col;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vec vector = (Vec) o;
            return row == vector.row && col == vector.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return String.format("(%+d, %+d)", row, col);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/tenth.in"));
        var field = new ArrayList<char[]>();
        while (in.hasNextLine()) {
            field.add(in.nextLine().toCharArray());
        }

        int n = field.size();
        int m = field.get(0).length;
        var graph = new Vecs[n][m];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                graph[i][j] = new Vecs();
            }
        }
        int sRow = -1, sCol = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < field.get(i).length; j++) {
                if (field.get(i)[j] == 'S') {
                    // Special case. Don't try to figure out direction.
                    // We will derive it later.
                    sRow = i;
                    sCol = j;
                } else {
                    graph[i][j] = Vecs.fromChar(field.get(i)[j]);
                }
            }
        }
        if (sRow == -1) {
            throw new IllegalStateException("There is no S on the board");
        }

        for (Vec delta : Vec.adjacent()) {
            int i = sRow + delta.row;
            int j = sCol + delta.col;

            if (i >= 0 && i < n && j >= 0 && j < m) {
                if (graph[i][j].vectors.contains(delta.inv())) {
                    graph[sRow][sCol].vectors.add(delta);
                }
            }
        }

        // Assign S a letter.
        for (char potential : List.of('-', '|', 'L', 'J', '7', 'F')) {
            var trying = Vecs.fromChar(potential);
            if (graph[sRow][sCol].equals(trying)) {
                field.get(sRow)[sCol] = potential;
                break;
            }
        }

        var deque = new ArrayDeque<Vec>();
        var visited = new HashSet<Vec>();

        // . - ?
        // * - loop
        // I - inside
        // O - outside
        char[][] mark = new char[n][m];
        for (char[] chars : mark) {
            Arrays.fill(chars, '.');
        }
        deque.add(new Vec(sRow, sCol));
        while (!deque.isEmpty()) {
            var current = deque.removeFirst();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            mark[current.row][current.col] = '*';

            for (Vec delta : graph[current.row][current.col].vectors) {
                var next = current.add(delta);
                if (next.row >= 0 && next.row < n && next.col >= 0 && next.col < m) {
                    if (!visited.contains(next)) {
                        deque.addLast(next);
                    }
                }
            }
        }

        var horizontalFull = List.of('|');
        var verticalFull = List.of('-');
        var halves = List.of('F', 'J', '7', 'L');
        var horizontalPartial = List.of(List.of('F', 'J'), List.of('L', '7'));
        var verticalPartial = List.of(List.of('F', 'J'), List.of('7', 'L'));
        // A dot is enclosed within a loop, if in every direction to the surface
        // it passes through odd number of loop pipes.
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mark[i][j] == '*') {
                    // Only parts which are NOT MAIN LOOP are interesting.
                    continue;
                }
                boolean inside = true;
                for (var delta : Vec.adjacent()) {
                    int I = i, J = j;
                    int toEdge = 0;
                    var full = (delta.row == 0 ? horizontalFull : verticalFull);
                    var partial = (delta.row == 0 ? horizontalPartial : verticalPartial);
                    var plus = delta.row > 0 || delta.col > 0;
                    char previous = '.';
                    while (true) {
                        I += delta.row;
                        J += delta.col;
                        if (I < 0 || I == n || J < 0 || J == m) {
                            break;
                        }
                        if (mark[I][J] != '*') {
                            // Not a loop. Reset.
                            previous = '.';
                            continue;
                        }
                        // Encountered full pipe.
                        char current = field.get(I)[J];
                        if (full.contains(current)) {
                            toEdge++;
                            previous = '.';
                            continue;
                        }
                        // Finished partial enclosure.
                        final var p = previous;
                        if (partial.stream().anyMatch(o -> {
                                    if (plus) {
                                        return o.get(0) == p && o.get(1) == current;
                                    } else {
                                        return o.get(0) == current && o.get(1) == p;
                                    }
                                }
                        )) {
                            toEdge++;
                            previous = '.';
                            continue;
                        }
                        if (halves.contains(current)) {
                            previous = current;
                        }
                    }

                    if (toEdge % 2 == 0) {
                        inside = false;
                    }
                }
                mark[i][j] = (inside ? 'I' : 'O');
                result += (inside ? 1 : 0);
            }

        }
        System.out.println(result);
    }


}