package pro.prieran.misis.chm.second_homework.gradient;

import pro.prieran.misis.chm.Utils;

import java.util.Arrays;

import static java.lang.Math.pow;

public class Gradient {
    private static final int COUNT_OF_DIGIT = 5;
    private static final double EPS = pow(10, -(COUNT_OF_DIGIT + 1));
    private static final double EPS_DIV = 1E-8;

    public static void main(String[] args) {
        double[] startValues = {2, 1, 2};
        double[] newValues = new double[startValues.length];
        double lambda = 1;

        for (int k = 0; k < 10_000; k++) {
            double[] grad = grad(startValues);

            for (int i = 0; i < startValues.length; i++) {
                newValues[i] = startValues[i] - lambda * grad[i];
            }

            double norma = 0.0;
            for (int i = 0; i < startValues.length; i++) {
                norma += grad[i] * grad[i];
            }

            while (func(newValues) > func(startValues) - 0.1 * lambda * norma) {
                lambda *= 0.8;

                for (int i = 0; i < newValues.length; i++) {
                    newValues[i] = startValues[i] - lambda * grad[i];
                }
            }

            boolean isShouldStop = true;
            for (int i = 0; i < newValues.length; i++) {
                isShouldStop &= Math.abs(newValues[i] - startValues[i]) < EPS;
            }

            if (isShouldStop) {
                break;
            }

            startValues = Arrays.copyOf(newValues, startValues.length);

            if (k % 100 == 0) {
                System.out.print(k + ". ");
                Utils.printArray(startValues, COUNT_OF_DIGIT);
                System.out.println();
            }
        }
    }

    private static double func(double[] args) {
        double x = args[0];
        double y = args[1];
        double z = args[2];
        return pow(f1(x, y, z), 2.0) + pow(f2(x, y, z), 2.0) + pow(f3(x, y, z), 2.0);
    }

    private static double f1(double x, double y, double z) {
        return x + 0.02 * Math.pow(y, 2.0) - 0.005 * Math.pow(z, 2.0) - 1;
    }

    private static double f2(double x, double y, double z) {
        return y - 0.1 * z - 0.2 * Math.pow(x, 2.0) - 1;
    }

    private static double f3(double x, double y, double z) {
        return z - 0.2 * Math.pow(y, 4.0) - 0.0125 * Math.pow(x, 2.0) - 2;
    }

    private static double[] grad(double[] args) {
        double[] grad = new double[args.length];
        for (int i = 0; i < grad.length; i++) {
            double[] tempArgs = Arrays.copyOf(args, args.length);
            tempArgs[i] += EPS_DIV;
            grad[i] = (func(tempArgs) - func(args)) / EPS_DIV;
        }

        return grad;
    }
}