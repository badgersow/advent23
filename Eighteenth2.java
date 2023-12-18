import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Eighteenth2 {

    static class Ranges {
        // Positive disjolong ranges.
        List<Range> ranges = new ArrayList<>();

        public Ranges() {
        }

        public Ranges(List<Range> ranges) {
            this.ranges = ranges;
        }

        static class Point implements Comparable<Point> {
            long pos;
            boolean isFrom;

            public Point(long pos, boolean isFrom) {
                this.pos = pos;
                this.isFrom = isFrom;
            }

            @Override
            public int compareTo(Point o) {
                // isFrom doesn't matter.
                return Long.compare(pos, o.pos);
            }
        }

        Ranges union(Ranges other) {
            var points = new ArrayList<Point>();
            for (Range range : ranges) {
                points.add(new Point(range.from, true));
                points.add(new Point(range.to, false));
            }
            for (Range range : other.ranges) {
                points.add(new Point(range.from, true));
                points.add(new Point(range.to, false));
            }
            Collections.sort(points);

            var union = new ArrayList<Range>();
            int open = 0;
            long previous = Long.MIN_VALUE / 2;
            for (var point : points) {
                if (open > 0 && previous < point.pos) {
                    union.add(new Range(previous, point.pos));
                }
                open += point.isFrom ? 1 : -1;
                previous = point.pos;
            }

            return collapse(union);
        }

        Ranges xor(Ranges other) {
            var points = new ArrayList<Point>();
            for (Range range : ranges) {
                points.add(new Point(range.from, true));
                points.add(new Point(range.to, false));
            }
            for (Range range : other.ranges) {
                points.add(new Point(range.from, true));
                points.add(new Point(range.to, false));
            }
            Collections.sort(points);

            var xored = new ArrayList<Range>();
            boolean on = false;
            long previous = Long.MIN_VALUE / 2;
            for (var point : points) {
                if (on && previous < point.pos) {
                    xored.add(new Range(previous, point.pos));
                }
                on ^= true;
                previous = point.pos;
            }

            return collapse(xored);
        }

        private static Ranges collapse(ArrayList<Range> xored) {
            if (xored.isEmpty()) {
                return new Ranges(xored);
            }

            // Let's collapse result
            Range prev = xored.getFirst();
            var collapsed = new ArrayList<Range>();
            for (int i = 1; i < xored.size(); i++) {
                Range current = xored.get(i);
                if (prev.to != current.from) {
                    collapsed.add(prev);
                    prev = current;
                } else {
                    prev = new Range(prev.from, current.to);
                }
            }
            collapsed.add(prev);

            return new Ranges(collapsed);
        }

        public long perimeter() {
            long result = 0;
            for (Range range : ranges) {
                result += range.to - range.from + 1;
            }
            return result;
        }

        @Override
        public String toString() {
            return ranges.toString();
        }
    }

    static class Range {
        long from, to;

        public Range(long from, long to) {
            if (from > to) {
                throw new IllegalStateException("Negative range: [" + from + ", " + to + "]");
            }
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "(%d, %d)".formatted(from, to);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/eighteenth.in"));

        var verticalByHorizontal = new TreeMap<Long, List<Range>>();
        // R 6 (#70c710)
        // D 5 (#0dc571)
        long i = 0;
        long prevJ = 0;
        while (in.hasNextLine()) {
            var line = in.nextLine();
            var length = Long.parseLong(line.substring(0, line.length() - 2).split("#")[1], 16);
            var dirChar = line.charAt(line.length() - 2);

            if (dirChar == '1') { // Down
                verticalByHorizontal.putIfAbsent(prevJ, new ArrayList<>());
                long I = i - length;
                verticalByHorizontal.get(prevJ).add(new Range(I, i));
                i = I;
            } else if (dirChar == '3') { // Up
                verticalByHorizontal.putIfAbsent(prevJ, new ArrayList<>());
                long I = i + length;
                verticalByHorizontal.get(prevJ).add(new Range(i, I));
                i = I;
            } else if (dirChar == '2') { // Left
                prevJ -= length;
            } else if (dirChar == '0') { // Right
                prevJ += length;
            } else {
                throw new IllegalStateException("Unknown dirChar" + dirChar);
            }
        }

        prevJ = Long.MIN_VALUE / 2;
        Ranges ranges = new Ranges();
        long result = 0;
        for (var pair : verticalByHorizontal.entrySet()) {
            var nextJ = pair.getKey();
            var line = new Ranges(pair.getValue());

            // Adding previous perimeter.
            result += ranges.perimeter() * Math.max(0, nextJ - prevJ - 1);

            // Adding current line.
            var union = ranges.union(line);
            result += union.perimeter(); // Adding current line.

            prevJ = nextJ;
            ranges = ranges.xor(line);
        }

        System.out.println(result);
    }

}
