import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Seventeenth2 {

    static final int INF = Integer.MAX_VALUE / 2;

    static final int minBeforeTurn = 4;

    static final int maxBeforeTurn = 10;

    static class Vec {
        int i, j;

        static Vec DOWN = new Vec(1, 0);
        static Vec UP = new Vec(-1, 0);
        static Vec LEFT = new Vec(0, -1);
        static Vec RIGHT = new Vec(0, 1);

        public Vec(int i, int j) {
            this.i = i;
            this.j = j;
        }

        Vec add(Vec other) {
            return new Vec(i + other.i, j + other.j);
        }

        boolean inBounds(int n, int m) {
            return i >= 0 && i < n && j >= 0 && j < m;
        }


        boolean vertical() {
            return i != 0;
        }

        List<Vec> turn() {
            if (vertical()) {
                return List.of(LEFT, RIGHT);
            }
            return List.of(UP, DOWN);
        }

        @Override
        public String toString() {
            return "(%d, %d)".formatted(i, j);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vec vec = (Vec) o;
            return i == vec.i && j == vec.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    static class Node {
        Vec position;
        Vec direction;
        int straight;

        public Node(Vec position, Vec direction, int straight) {
            this.position = position;
            this.direction = direction;
            this.straight = straight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return straight == node.straight && Objects.equals(position, node.position) && Objects.equals(direction, node.direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction, straight);
        }
    }

    static class Distance implements Comparable<Distance> {
        Node node;
        int distance;

        public Distance(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(Distance other) {
            return Integer.compare(distance, other.distance);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/seventeenth.in"));

        var arrList = new ArrayList<int[]>();
        while (in.hasNextLine()) {
            arrList.add(in.nextLine().chars().map(c -> c - '0').toArray());
        }
        int[][] arr = arrList.toArray(new int[][]{});
        int n = arr.length;
        int m = arr[0].length;
        var d = new HashMap<Node, Integer>();
        var queue = new PriorityQueue<Distance>();

        for (var dir : List.of(Vec.DOWN, Vec.RIGHT)) {
            var node = new Node(new Vec(0, 0), dir, 0);
            d.put(node, 0);
            queue.add(new Distance(node, 0));
        }

        while (!queue.isEmpty()) {
            var current = queue.remove();
            var node = current.node;
            var dist = current.distance;
            var here = node.position;
            var dir = node.direction;
            var st = node.straight;

            // Is this the exit?
            if (here.i == n - 1 && here.j == n - 1) {
                if (st >= minBeforeTurn) {
                    // This is the result.
                    System.out.println(dist);
                    return;
                } else {
                    // We are at the end, but can't stop.
                    // Nothing can be done.
                    continue;
                }
            }

            // Can I do step forward?
            if (st < maxBeforeTurn) {
                var there = here.add(dir);
                var other = new Node(there, dir, st + 1);
                if (there.inBounds(n, m)) {
                    var tryDist = dist + arr[there.i][there.j];
                    if (tryDist < d.getOrDefault(other, INF)) {
                        d.put(other, tryDist);
                        queue.add(new Distance(other, tryDist));
                    }
                }
            }

            // Can I turn
            if (st >= minBeforeTurn) {
                for (var turn : dir.turn()) {
                    var there = here.add(turn);
                    var other = new Node(there, turn, 1);
                    if (there.inBounds(n, m)) {
                        var tryDist = dist + arr[there.i][there.j];
                        if (tryDist < d.getOrDefault(other, INF)) {
                            d.put(other, tryDist);
                            queue.add(new Distance(other, tryDist));
                        }
                    }
                }
            }
        }
    }
}
