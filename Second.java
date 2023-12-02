import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Second {
    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/second.in"));
        var result = 0;
        var maxByColor = Map.of("red", 12, "green", 13, "blue", 14);
        outer: while (in.hasNextLine()) {
            var line = in.nextLine();
            var gameDesc = line.split(": ")[1];
            var games = gameDesc.split("; ");
            for (var game : games) {
                var colors = game.split(", ");
                for (var color : colors) {
                    var splitColor = color.split(" ");
                    if (!maxByColor.containsKey(splitColor[1])) {
                        throw new IllegalArgumentException("Can't find color in settings");
                    }
                    var currentMax = maxByColor.get(splitColor[1]);
                    if (Long.parseLong(splitColor[0]) >currentMax) {
                        // Impossible
                        continue outer;
                    }
                }
            }
            var gameNum = line.split(": ")[0].split(" ")[1];
            result += Long.parseLong(gameNum);
        }

        System.out.println(result);
    }
}
