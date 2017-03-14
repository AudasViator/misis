package pro.prieran.misis.chm.monte_carlo;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.function.Function;

public class Main {
    private static final int COUNT_OF_ALL_POINTS = 100_000;

    public static void main(String[] args) throws Exception {
        manyDimensions();
        oneDimension();
    }

    private static double func(double[] params) {
        double x = params[0];
        double y = params[1];
        double z = params[2];
        return Math.pow(x, 3) + Math.pow(y, 2) + Math.log(z);
    }

    private static void manyDimensions() {
        Pcg32 random = new Pcg32();

        final int countOfDimens = 3;
        final double EPS_BOX = 1E-4;

        double[] from = {3, 2, 1};
        double[] to = {5, 4, 3};

        double boxMax = Double.NEGATIVE_INFINITY;

        double[] tempFrom = Arrays.copyOf(from, countOfDimens);
        for (int i = 0; i < countOfDimens; i++) {
            for (; tempFrom[i] <= to[i]; tempFrom[i] += EPS_BOX) {
                boxMax = Math.max(boxMax, func(tempFrom));
            }
        }

        double boxSquare = 1.0;
        boxSquare *= boxMax;
        for (int i = 0; i < countOfDimens; i++) {
            boxSquare *= (to[i] - from[i]);
        }

        int countOfGoodPoints = 0; // Те, что под графиком
        for (int i = 0; i < COUNT_OF_ALL_POINTS; i++) {
            double[] point = new double[countOfDimens + 1];
            for (int j = 0; j < countOfDimens; j++) {
                point[j] = from[j] + random.nextDouble((to[j] - from[j]));
            }
            point[countOfDimens] = random.nextDouble(boxMax);


            double[] pointArg = Arrays.copyOf(point, countOfDimens);
            if (point[countOfDimens] <= func(pointArg)) {
                countOfGoodPoints++;
            }

//            if (i % (COUNT_OF_ALL_POINTS / 100) == 0) {
//                double currSquare = boxSquare * countOfGoodPoints / i;
//                System.out.printf(Locale.getDefault(), "%d;%f\n", i, currSquare);
//            }
        }

        double square = boxSquare * countOfGoodPoints / COUNT_OF_ALL_POINTS;
        System.out.println("Result: " + square);
    }

    private static void oneDimension() throws FileNotFoundException {
        Pcg32 random = new Pcg32();

        final Function<Double, Double> func = x -> 3 + Math.sin(x) * Math.exp(Math.cos(x * x / 10));

        double from = 0.0;
        double to = 20.0;

        double average = 0.0;
        for (int i = 0; i < COUNT_OF_ALL_POINTS; i++) {
            double value = from + random.nextDouble(to - from);
            average += func.apply(value);
        }
        average /= COUNT_OF_ALL_POINTS;
        average *= (to - from);
        System.out.println("Result: " + average);
    }
}