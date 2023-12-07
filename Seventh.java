import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Seventh {

    static String frequencies(String hand) {
        var freq = new HashMap<Character, Integer>();
        for (char c : hand.toCharArray()) {
            freq.compute(c, (k, v) -> {
                if (v == null) return 1;
                return v + 1;
            });
        }
        return freq.values().stream()
                .sorted(Comparator.reverseOrder())
                .map(i -> i + "")
                .collect(Collectors.joining(""));
    }

    static boolean isFive(String s) {
        return "5".equals(frequencies(s));
    }

    static boolean isFour(String s) {
        return "41".equals(frequencies(s));
    }

    static boolean isFullHouse(String s) {
        return "32".equals(frequencies(s));
    }

    static boolean isThree(String s) {
        return "311".equals(frequencies(s));
    }

    static boolean isTwoPair(String s) {
        return "221".equals(frequencies(s));
    }

    static boolean isOnePair(String s) {
        return "2111".equals(frequencies(s));
    }

    static boolean isHighCard(String s) {
        return "11111".equals(frequencies(s));
    }

    static int score(String hand) {
        List<Predicate<String>> combinations = List.of(
                Seventh::isHighCard,
                Seventh::isOnePair,
                Seventh::isTwoPair,
                Seventh::isThree,
                Seventh::isFullHouse,
                Seventh::isFour,
                Seventh::isFive
        );
        for (int i = 0; i < combinations.size(); i++) {
            if (combinations.get(i).test(hand)) {
                return i;
            }
        }
        throw new IllegalStateException("Some combination must have been selected");
    }

    static String rewriteHand(String hand) {
        var mapping = new HashMap<Character, Character>();
        mapping.put('2', 'a');
        mapping.put('3', 'b');
        mapping.put('4', 'c');
        mapping.put('5', 'd');
        mapping.put('6', 'e');
        mapping.put('7', 'f');
        mapping.put('8', 'g');
        mapping.put('9', 'h');
        mapping.put('T', 'i');
        mapping.put('J', 'j');
        mapping.put('Q', 'k');
        mapping.put('K', 'l');
        mapping.put('A', 'm');
        var result = new StringBuilder();
        for (char c : hand.toCharArray()) {
            result.append(mapping.get(c));
        }
        return result.toString();
    }

    static class Bid {
        public Bid(String hand, long bet) {
            this.hand = hand;
            this.bet = bet;
        }

        String hand;
        long bet;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/seventh.in"));

        var bids = new ArrayList<Bid>();
        while (in.hasNextLine()) {
            var line = in.nextLine();
            var bid = new Bid(
                    rewriteHand(line.split(" ")[0]),
                    Long.parseLong(line.split(" ")[1]));
            bids.add(bid);
        }

        bids.sort(
                Comparator.<Bid>comparingInt(
                                bid -> score(bid.hand))
                        .thenComparing(bid -> bid.hand));

        long total = 0;
        for (int i = 0; i < bids.size(); i++) {
            total += bids.get(i).bet * (i + 1);
        }

        System.out.println(total);
    }
}
