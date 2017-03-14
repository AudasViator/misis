package pro.prieran.misis.chm.second_homework.gradient;

import pro.prieran.misis.chm.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.pow;

class Gradient {
    private final static int COUNT_OF_DIGIT = 5;
    private final static double EPS = pow(10, -(COUNT_OF_DIGIT + 1));
    private final static double EPS_DIV = 1E-8;

    List<Double> xPoints = new ArrayList<>();
    List<Double> yPoints = new ArrayList<>();
    List<Double> zPoints = new ArrayList<>();


    double f1(double x, double y, double z) {
        return x + 0.02 * Math.pow(y, 2.0) - 0.005 * Math.pow(z, 2.0) - 1;
    }

    double f2(double x, double y, double z) {
        return y - 0.1 * z - 0.2 * Math.pow(x, 2.0) - 1;
    }

    double f3(double x, double y, double z) {
        return z - 0.2 * Math.pow(y, 4.0) - 0.0125 * Math.pow(x, 2.0) - 2;
    }

    void doIt() {
        double[] startValues = {0, 0, 0};
        double[] newValues = new double[startValues.length];
        double lambda = 1;

        for (int k = 0; k < 10_000; k++) {
            xPoints.add(startValues[0]);
            yPoints.add(startValues[1]);
            zPoints.add(startValues[2]);

            double[] grad = grad(startValues);

            for (int i = 0; i < startValues.length; i++) {
                newValues[i] = startValues[i] - lambda * grad[i];
            }

            double norm = 0.0;
            for (int i = 0; i < startValues.length; i++) {
                norm += grad[i] * grad[i];
            }

            while (func(newValues) > func(startValues) - 0.1 * lambda * norm) {
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

    private double func(double[] args) {
        double x = args[0];
        double y = args[1];
        double z = args[2];
        return pow(f1(x, y, z), 2.0) + pow(f2(x, y, z), 2.0) + pow(f3(x, y, z), 2.0);
    }

    private double[] grad(double[] args) {
        double[] grad = new double[args.length];
        for (int i = 0; i < grad.length; i++) {
            double[] tempArgs = Arrays.copyOf(args, args.length);
            tempArgs[i] += EPS_DIV;
            grad[i] = (func(tempArgs) - func(args)) / EPS_DIV;
        }

        return grad;
    }
}