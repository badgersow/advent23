import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Second2 {
    public static void main(String[] args) throws FileNotFoundException {
        var in = new Scanner(new File("data/second.in"));
        var result = 0;
        outer:
        while (in.hasNextLine()) {
            var possibleMin = new HashMap<String, Long>();
            var line = in.nextLine();
            var gameDesc = line.split(": ")[1];
            var games = gameDesc.split("; ");
            for (var game : games) {
                var colors = game.split(", ");
                for (var color : colors)/**/ {
                    var splitColor = color.split(" ");
                    var name = splitColor[1];
                    var value = Long.parseLong(splitColor[0]);
                    var currentMin = possibleMin.getOrDefault(name, -1L);
                    possibleMin.put(name, Math.max(currentMin, value));
                }
            }
            result += possibleMin.get("red") * possibleMin.get("green") * possibleMin.get("blue");
        }

        System.out.println(result);
    }
}
