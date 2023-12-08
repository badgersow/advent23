import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

public class Eights2 {

    static Map<String, String> left = new HashMap<>();

    static Map<String, String> right = new HashMap<>();

    static char[] string;

    static Solution solve(String starting) {
        var result = new Solution();
        var node = starting;
        var positionByNode = new HashMap<Position, Integer>();
        int index = 0;
        int iteration = 0;
        while (true) {
            positionByNode.put(new Position(index, node), iteration);
            var toFollow = (string[index] == 'L' ? left : right);
            index = (index + 1) % string.length;
            var next = toFollow.get(node);
            if (next.endsWith("Z")) {
                result.matches.add(iteration + 1);
            }
            if (positionByNode.containsKey(new Position(index, next))) {
                // Cycle detected!
                result.loopback = iteration;
                result.target = positionByNode.get(new Position(index, next));
                return result;
            }
            node = next;
            iteration++;
        }
    }

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

    static class Solution {
        // Positions of node ending with Z.
        List<Integer> matches = new ArrayList<>();
        // Position of node which loops on previous node.
        int loopback;
        // Position of node on WHICH the loopback loops to.
        int target;

        @Override
        public String toString() {
            return "Solution{" +
                    "matches=" + matches +
                    ", loopback=" + loopback +
                    ", target=" + target +
                    '}';
        }
    }

    public static <List> void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/eights.in"));

        string = in.nextLine().toCharArray();
        in.nextLine();
        var nodes = new ArrayList<String>();
        while (in.hasNextLine()) {
            var line = in.nextLine();
            var node = line.split(" = ")[0];
            var pathLeft = line.split(" = ")[1].split(", ")[0].substring(1);
            var pathRight = line.split(" = ")[1].split(", ")[1];
            pathRight = pathRight.substring(0, pathRight.length() - 1);

            left.put(node, pathLeft);
            right.put(node, pathRight);
            if (node.endsWith("A")) {
                nodes.add(node);
            }
        }

//        JQA: Solution{matches=[13939], loopback=13941, target=3}
//        NHA: Solution{matches=[11309], loopback=11311, target=3}
//        AAA: Solution{matches=[20777], loopback=20778, target=2}
//        FSA: Solution{matches=[15517], loopback=15518, target=2}
//        LLA: Solution{matches=[17621], loopback=17624, target=4}
//        MNA: Solution{matches=[18673], loopback=18674, target=2}
        var matches = new ArrayList<Long>();
        for (String n : nodes) {
            matches.add(solve(n).matches.getFirst().longValue());
        }

        long lcm = matches.getFirst();
        for (int i = 1; i < matches.size(); i++) {
            var current = BigInteger.valueOf(matches.get(i));
            lcm = BigInteger.valueOf(lcm).multiply(current)
                    .divide(BigInteger.valueOf(lcm).gcd(current)).longValue();
        }

        System.out.println(lcm);
    }
}
