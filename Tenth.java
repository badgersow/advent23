import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Tenth {

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
            this.vectors = vectors;
        }

        public Vecs() {
        }

        List<Vec> vectors = new ArrayList<>();

        @Override
        public String toString() {
            return vectors.toString();
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

    static class Cell {

        public Cell(Vec vec, int dist) {
            this.vec = vec;
            this.dist = dist;
        }

        Vec vec;
        int dist;

        @Override
        public String toString() {
            return "Cell{" +
                    "vec=" + vec +
                    ", dist=" + dist +
                    '}';
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

        var deque = new ArrayDeque<Cell>();
        var visited = new HashSet<Vec>();
        int[][] dist = new int[n][m];
        deque.add(new Cell(new Vec(sRow, sCol), 0));
        while (!deque.isEmpty()) {
            var current = deque.removeFirst();
            if (visited.contains(current.vec)) {
                continue;
            }
            visited.add(current.vec);
            dist[current.vec.row][current.vec.col] = current.dist;

            for (Vec delta : graph[current.vec.row][current.vec.col].vectors) {
                var next = current.vec.add(delta);
                if (next.row >= 0 && next.row < n && next.col >= 0 && next.col < m) {
                    if (!visited.contains(next)) {
                        deque.addLast(new Cell(next, current.dist + 1));
                    }
                }
            }
        }

        int max = 0;
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[i].length; j++) {
                max = Math.max(max, dist[i][j]);
            }
        }
        System.out.println(max);
    }


}