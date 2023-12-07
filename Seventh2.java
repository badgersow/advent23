import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Seventh2 {

    static String frequencies(String hand) {
        var freq = new HashMap<Character, Integer>();
        int jokers = 0;
        for (char c : hand.toCharArray()) {
            if (c == 'J') {
                jokers++;
                continue;
            }
            freq.compute(c, (k, v) -> {
                if (v == null) return 1;
                return v + 1;
            });
        }

        if (jokers == 5) {
            // Edge case, all jokers.
            return "5";
        }

        List<Integer> freqList = freq.values().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        // Jokers always go to the biggest current number;
        freqList.set(0, freqList.get(0) + jokers);
        return freqList.stream().map(i -> "" + i).collect(Collectors.joining());
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
                Seventh2::isHighCard,
                Seventh2::isOnePair,
                Seventh2::isTwoPair,
                Seventh2::isThree,
                Seventh2::isFullHouse,
                Seventh2::isFour,
                Seventh2::isFive
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
        mapping.put('J', 'a');
        mapping.put('2', 'b');
        mapping.put('3', 'c');
        mapping.put('4', 'd');
        mapping.put('5', 'e');
        mapping.put('6', 'f');
        mapping.put('7', 'g');
        mapping.put('8', 'h');
        mapping.put('9', 'i');
        mapping.put('T', 'j');
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
                    line.split(" ")[0],
                    Long.parseLong(line.split(" ")[1]));
            bids.add(bid);
        }

        bids.sort(
                Comparator.<Bid>comparingInt(
                                bid -> score(bid.hand))
                        .thenComparing(bid -> rewriteHand(bid.hand)));

        long total = 0;
        for (int i = 0; i < bids.size(); i++) {
            total += bids.get(i).bet * (i + 1);
        }

        System.out.println(total);
    }
}
