import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Eleventh {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/eleventh.in"));

        var galaxies = new ArrayList<int[]>();
        var hasGalaxyI = new HashSet<Integer>();
        var hasGalaxyJ = new HashSet<Integer>();

        int n = 0;
        int m = 0;
        while (in.hasNextLine()) {
            var line = in.nextLine().toCharArray();
            m = line.length;
            for (int j = 0; j < m; j++) {
                if (line[j] == '#') {
                    galaxies.add(new int[]{n, j});
                    hasGalaxyI.add(n);
                    hasGalaxyJ.add(j);
                }
            }
            n++;
        }

        int[] noGalaxiesUpToI = new int[n + 1];
        int[] noGalaxiesUpToJ = new int[m + 1];

        for (int i = 1; i < (n + 1); i++) {
            noGalaxiesUpToI[i] = noGalaxiesUpToI[i - 1] + (hasGalaxyI.contains(i - 1) ? 0 : 1);
        }
        for (int j = 1; j < (m + 1); j++) {
            noGalaxiesUpToJ[j] = noGalaxiesUpToJ[j - 1] + (hasGalaxyJ.contains(j - 1) ? 0 : 1);
        }

        long resultTwice = 0;
        for (var g1 : galaxies) {
            for (var g2 : galaxies) {
                // Vertical
                var i1 = g1[0] + noGalaxiesUpToI[g1[0]];
                var i2 = g2[0] + noGalaxiesUpToI[g2[0]];
                resultTwice += Math.abs(i1 - i2);

                // Horizontal
                var j1 = g1[1] + noGalaxiesUpToJ[g1[1]];
                var j2 = g2[1] + noGalaxiesUpToJ[g2[1]];
                resultTwice += Math.abs(j1 - j2);
            }
        }

        System.out.println(resultTwice / 2);
    }
}
