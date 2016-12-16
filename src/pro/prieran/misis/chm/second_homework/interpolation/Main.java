package pro.prieran.misis.chm.second_homework.interpolation;

import java.util.function.Function;

public class Main {

    private static final Function<Double, Double> FUNC = x -> Math.log(x * Math.log(1 + x)) / x;
    private static final int COUNT_OF_POINTS = 50;
    private static final double FROM = 1.0;
    private static final double TO = 10.0;

    public static void main(String[] args) {
        double[][] diffs = new double[COUNT_OF_POINTS + 1][];

        diffs[0] = new double[COUNT_OF_POINTS]; // Здесь хранятся иксы
        diffs[1] = new double[COUNT_OF_POINTS]; // Здесь хранятся значения функции

        double step = (TO - FROM) / COUNT_OF_POINTS;
        double current = FROM;
        for (int i = 0; i < diffs[0].length; i++) {
            diffs[0][i] = current;
            current += step;
        }

        for (int i = 0; i < diffs[0].length; i++) {
            diffs[1][i] = FUNC.apply(diffs[0][i]);
        }

        for (int i = 2; i < diffs.length; i++) {
            diffs[i] = new double[diffs.length - i];
            for (int j = 0; j < diffs[i].length; j++) {
                diffs[i][j] = (diffs[i - 1][j] - diffs[i - 1][j + 1]) / (diffs[0][j] - diffs[0][i + j - 1]);
            }
        }

        double value = 0.0;
        double x = 1.22;
        for (int i = 0; i < COUNT_OF_POINTS; i++) {
            double diff = 1.0;
            for (int j = 0; j < i; j++) {
                diff *= (x - diffs[0][j]);
            }
            value += diffs[i + 1][0] * diff;
        }
        System.out.println("x = " + x + ", f = " + value + ", true = " + FUNC.apply(x));
    }
}