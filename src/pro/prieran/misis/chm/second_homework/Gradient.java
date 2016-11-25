package pro.prieran.misis.chm.second_homework;

import pro.prieran.misis.chm.Utils;

import java.util.Arrays;

import static java.lang.Math.pow;

public class Gradient {
    private static final int COUNT_OF_DIGIT = 5;
    private static final double EPS = pow(10, -(COUNT_OF_DIGIT + 1));

    public static void main(String[] args) {
        double[] startValues = {2, 1, 2};
        double[] newValues = new double[startValues.length];
        double lambda = 1;

        for (int k = 0; k < 100_000; k++) {
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

        /*
        1)x^2-y/2-cos(z)=0;
        2)x^2+y^2-0.5=0;
        3)-(2*x*pi^3)/27 + 2*y + z^3-1=0;
        x=1/2 y=-12 z=pi/3

        F = f^2 + g^2 + k^2

        1)x^2+y^3=10
        2)x^3-z+y^3=0
        3)x^3+z=10

        x = 0;    y = 2.15443;  z = 10
        x = 0.5;  y = 2.13633;  z = 9.875

        F = (x^2+y^3-10)^2 + (x^3-z+y^3)^2 + (x^3+z-10)^2
        F = 2 z^2 - 2 y^3 z - 20 z + 2 y^6 + 2 x^3 y^3 + 2 x^2 y^3 - 20 y^3 + 2 x^6 + x^4 - 20 x^3 - 20 x^2 + 200
        gradX = 2 x (2 x^2 + 6 x^4 + 2 (-10 + y^3) + 3 x (-10 + y^3))
        gradY = 6 y^2 (-10+x^2+x^3+2 y^3-z)
        gradZ = -2 (10 + y^3 - 2 z)
     */

    private static double func(double[] args) {
        double x = args[0];
        double y = args[1];
        double z = args[2];
        return pow((pow(x, 2) + pow(y, 3) - 10), 2) + pow((pow(x, 3) - z + pow(y, 3)), 2) + pow((pow(x, 3) + z - 10), 2);
    }

    private static double[] grad(double[] args) {
        double x = args[0];
        double y = args[1];
        double z = args[2];
        return new double[]{
                2 * x * (2 * pow(x, 2) + 6 * pow(x, 4) + 2 * (-10 + pow(y, 3)) + 3 * x * (-10 + pow(y, 3))),
                6 * pow(y, 2) * (-10 + pow(x, 2) + pow(x, 3) + 2 * pow(y, 3) - z),
                -2 * (10 + pow(y, 3) - 2 * z)
        };
    }
}
