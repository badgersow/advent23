import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Fifteenth2 {

    static class Node {
        String label;
        int value;

        public Node(String label, int value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + label + value + "]";
        }
    }

    static class HM {
        ArrayList<Node>[] nodes = new ArrayList[256];

        public HM() {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new ArrayList<>();
            }
        }

        static int hash(String s) {
            int result = 0;
            for (char c : s.toCharArray()) {
                result += c;
                result *= 17;
                result %= 256;
            }
            return result;
        }

        void put(String label, int value) {
            int hash = hash(label);
            for (int i = 0; i < nodes[hash].size(); i++) {
                if (nodes[hash].get(i).label.equals(label)) {
                    nodes[hash].get(i).value = value;
                    return;
                }
            }
            nodes[hash].add(new Node(label, value));
        }

        void remove(String label) {
            int hash = hash(label);
            int position = -1;
            for (int i = 0; i < nodes[hash].size(); i++) {
                if (nodes[hash].get(i).label.equals(label)) {
                    position = i;
                }
            }
            if (position == -1) {
                return;
            }
            nodes[hash].remove(position);
        }

        long power() {
            long result = 0;
            for (int i = 0; i < nodes.length; i++) {
                for (int j = 0; j < nodes[i].size(); j++) {
                    var node = nodes[i].get(j);
                    result += (i + 1L) * (j + 1L) * node.value;
                }
            }
            return result;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();
            for (int i = 0; i < nodes.length; i++) {
                if (!nodes[i].isEmpty()) {
                    sb
                            .append("Box ")
                            .append(i)
                            .append(": ")
                            .append(nodes[i])
                            .append('\n');
                }
            }
            return sb.toString();
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/fifteenth.in"));

        var strings = in.nextLine().split(",");
        var hm = new HM();
        for (var command : strings) {
            if (command.endsWith("-")) {
                hm.remove(command.substring(0, command.length() - 1));
            }
            if (command.contains("=")) {
                var split = command.split("=");
                hm.put(split[0], Integer.parseInt(split[1]));
            }
        }
        System.out.println(hm.power());
    }
}
