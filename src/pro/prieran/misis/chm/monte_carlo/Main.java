package pro.prieran.misis.chm.monte_carlo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws Exception {
        Pcg32 random = new Pcg32();

        final int countOfDimens = 3;
        final double EPS_BOX = 1E-4;

        double[] from = {0, 0, 0};
        double[] to = {2, 3, 4};

        double boxMax = Double.NEGATIVE_INFINITY;
        double boxMin = Double.POSITIVE_INFINITY;

        double[] tempFrom = Arrays.copyOf(from, countOfDimens);
        for (int i = 0; i < countOfDimens; i++) {
            for (; tempFrom[i] <= to[i]; tempFrom[i] += EPS_BOX) {
                boxMax = Math.max(boxMax, func(tempFrom));
                boxMin = Math.min(boxMin, func(tempFrom));
            }
        }

        final int countOfAllPoints = 100_000;
        int countOfGoodPoints = 0; // Те, что под графиком

        for (int i = 0; i < countOfAllPoints; i++) {
            double[] point = new double[countOfDimens + 1];
            for (int j = 0; j < countOfDimens; j++) {
                point[j] = from[j] + random.nextDouble((to[j] - from[j]));
            }
            point[countOfDimens] = boxMin + random.nextDouble(boxMax - boxMin);

            double[] pointArg = Arrays.copyOf(point, countOfDimens);
            if (point[countOfDimens] <= func(pointArg)) {
                countOfGoodPoints++;
            }
        }

        double boxSquare = 1.0;
        boxSquare *= (boxMax - boxMin);
        for (int i = 0; i < countOfDimens; i++) {
            boxSquare *= (to[i] - from[i]);
        }

        double square = boxSquare * countOfGoodPoints / countOfAllPoints;
        System.out.println(square);
    }

    private static double func(double[] params) {
        return Math.pow(params[0], 2) + Math.pow(params[1], 3) + Math.pow(params[2], 4);
    }

    private void oneDimension() throws FileNotFoundException {
        Pcg32 random = new Pcg32();

        Function<Double, Double> func = x -> Math.pow(x, 2);
        double from = 0.0;
        double to = 2.0;
        int countOfPoints = 100_000;

        PrintWriter writer = new PrintWriter("name.csv");

        for (int k = 0; k < 20; k++) {
            double average = 0.0;
            for (int i = 0; i < countOfPoints; i++) {
                double value = from + random.nextDouble(to - from);
                average += func.apply(value);
                if (i % 100 == 0) {
                    double currAvg = average / (i + 1);
                    currAvg *= (to - from);
                    writer.format(Locale.getDefault(), "%d;%f\n", i, currAvg);
                }
            }
            average /= countOfPoints;
            average *= (to - from);
            System.out.println(average);
        }

        writer.flush();
        writer.close();
    }
}