import java.io.*;
import java.util.*;
import java.util.List;

public class Fifth2 {

    public static class Mapping {

        public static final long INFINITY = Long.MAX_VALUE / 2;

        public Mapping(List<Point> points) {
            if (points.getFirst().pos != 0) {
                throw new IllegalArgumentException("Every mapping must have an entry for position 0");
            }
            if (points.getLast().pos != INFINITY) {
                throw new IllegalArgumentException("Every mapping must have an entry for position INFINITY");
            }
            this.points = List.copyOf(points);
        }

        // Produces mapping equivalent to this · other.
        public Mapping mergeWith(Mapping other) {
            var result = new ArrayList<Point>();

            // Every mapping has at least 2 elements.
            for (int i = 0; i < points.size() - 1; i++) {
                long fromL = points.get(i).pos;
                long toL = points.get(i + 1).pos;
                long deltaL = points.get(i).delta;

                // Iterate all elements in other sequence and find intersections.
                // TODO: can use binary search to find left border.
                for (int j = 0; j < other.points.size() - 1; j++) {
                    long fromR = other.points.get(j).pos;
                    long toR = other.points.get(j + 1).pos;
                    long deltaR = other.points.get(j).delta;

                    // What is the intersection (if any)?
                    long leftI = Math.max(fromL + deltaL, fromR);
                    long rightI = Math.min(toL + deltaL, toR);
                    if (leftI >= rightI) {
                        // No intersection.
                        continue;
                    }

                    // Otherwise, the intersection will produce a new record in the merged mapping.
                    result.add(new Point(leftI - deltaL, deltaL + deltaR));
                }
            }

            result.add(new Point(INFINITY, 0));
            return new Mapping(result);
        }

        // [pos[i] .. pos[i+1]) has delta[i]
        // Sorted by position and doesn't have duplicates. First point has position 0.
        // Last point has position INFINITY.
        List<Point> points;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Mapping mapping = (Mapping) o;
            return Objects.equals(points, mapping.points);
        }

        @Override
        public int hashCode() {
            return Objects.hash(points);
        }

        @Override
        public String toString() {
            return "" + points;
        }

        public long map(long seed) {
            // TODO: can use binary search.
            for (int i = 0; i < points.size() - 1; i++) {
                if (points.get(i).pos <= seed && seed < points.get(i + 1).pos) {
                    return seed + points.get(i).delta;
                }
            }
            throw new IllegalStateException("Every seed should have a mapping by construction");
        }

        public static class Point {
            public Point(long pos, long delta) {
                this.pos = pos;
                this.delta = delta;
            }

            long pos;
            long delta;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return pos == point.pos && delta == point.delta;
            }

            @Override
            public int hashCode() {
                return Objects.hash(pos, delta);
            }

            @Override
            public String toString() {
                return String.format("%d (%+d)\n", pos, delta);
            }
        }
    }

    public static boolean inSeedRange(List<Long> seeds, long pos) {
        // TODO: can be done with binary search.
        for (int i = 0; i < seeds.size(); i += 2) {
            if (pos >= seeds.get(i) && pos < seeds.get(i) + seeds.get(i + 1)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        var in = new BufferedReader(new InputStreamReader(new FileInputStream("data/fifth.in")));

        String seedsLine = in.readLine();
        List<Long> seeds = Arrays.stream(seedsLine.split(": ")[1].split(" "))
                .map(Long::parseLong)
                .toList();
        in.readLine(); // empty line.
        var mappings = new ArrayList<Mapping>();
        outer:
        while (true) {
            var first = in.readLine(); // foo-to-bar map: or empty
            if (first == null) {
                break;
            }

            // [l..r) = delta
            var input = new ArrayList<long[]>();
            while (true) {
                var line = in.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }

                var parsed = line.split(" ");
                var dest = Long.parseLong(parsed[0]);
                var src = Long.parseLong(parsed[1]);
                var len = Long.parseLong(parsed[2]);

                input.add(new long[]{src, src + len, dest - src});
            }

            input.sort(Comparator.comparingLong(a -> a[0]));
            var points = new ArrayList<Mapping.Point>();
            if (input.get(0)[0] != 0) {
                points.add(new Mapping.Point(0, 0));
            }
            for (int i = 0; i < input.size(); i++) {
                points.add(new Mapping.Point(input.get(i)[0], input.get(i)[2]));
                // Unless the second point overrides right delta, insert zero delta.
                if (i < input.size() - 1 && input.get(i + 1)[0] == input.get(i)[1]) {
                    continue;
                }
                points.add(new Mapping.Point(input.get(i)[1], 0));
            }
            points.add(new Mapping.Point(Mapping.INFINITY, 0L));

            mappings.add(new Mapping(points));
        }

        // Unit.
        Mapping merged = new Mapping(List.of(new Mapping.Point(0, 0), new Mapping.Point(Mapping.INFINITY, 0)));

        for (Mapping mapping : mappings) {
            merged = merged.mergeWith(mapping);
        }

        // Locations to try are:
        // - On the edges of seed ranges,
        // - On the edges of mapping points (if in seed range).
        var toTry = new ArrayList<Long>();
        for (int i = 0; i < seeds.size(); i += 2) {
            toTry.add(seeds.get(i));
            toTry.add(seeds.get(i) + seeds.get(i + 1) - 1);
        }

        for (var point : merged.points) {
            if (inSeedRange(seeds, point.pos)) {
                toTry.add(point.pos);
            }
        }

        long min_location = Long.MAX_VALUE;
        for (long seed : toTry) {
            long location = merged.map(seed);
            if (location < min_location) {
                min_location = location;
            }
        }

        System.out.println(min_location);
    }
}
