package pro.prieran.misis.chm.first_homework;

import pro.prieran.misis.chm.Utils;

import java.util.Arrays;

public class VariantSecond {

    public static void main(String[] args) {
//        double[][] matrix = {{5, 6, 3}, {-1, 0, 1}, {1, 2, -1}};  // 4, 2, -2
//        double[][] matrix = {{4, 1, 0}, {1, 2, 1}, {0, 1, 1}}; // 4.46, 2.24, 0.30
//        double[][] matrix = {{0, 2}, {3, 5}};  // 6, -1
        double[][] matrix = {{17, 6}, {6, 8}};  // 20, 5
//        double[][] matrix = {{2, 3, 2, 2}, {-1, -1, 0, -1}, {-2, -2, -2, -1}, {3, 2, 2, 2}}; // Обратная

        System.out.println("Inverted matrix:");
        Utils.printMatrix(invert(matrix));
        System.out.println();

        iteration(matrix);
    }

    private static double[][] invert(double matrix[][]) {
        double[][] a = Utils.copy(matrix);
        int n = a.length;
        double identity[][] = new double[n][n];
        for (int i = 0; i < n; ++i) {
            identity[i][i] = 1;
        }
        double[][] temp = new double[n][n];
        double inverse[][] = new double[n][n];

        for (int k = 0; k < n - 1; k++) {
            for (int i = k + 1; i < n; i++) {
                temp[i][k] = a[i][k] / a[k][k];
                for (int e = 0; e < n; e++) {
                    identity[i][e] -= temp[i][k] * identity[k][e];
                }

                for (int j = k + 1; j < n; j++) {
                    a[i][j] -= temp[i][k] * a[k][j];
                }
            }
        }

        for (int e = 0; e < n; e++) {
            inverse[n - 1][e] = identity[n - 1][e] / a[n - 1][n - 1];
            for (int k = n - 2; k >= 0; k--) {
                inverse[k] = identity[k];
                for (int j = k + 1; j < n; j++) {
                    inverse[k][e] -= a[k][j] * inverse[j][e];
                }
                inverse[k][e] /= a[k][k];
            }
        }

        return inverse;
    }

    private synchronized static double iteration(double[][] matrix) {
        final int MAX_ITERATION = 100;
        final double EPS = 1E-4;
        double lambda = Double.NaN;
        double[] y = new double[matrix.length];
        double[] x = new double[matrix.length];
        double[] xKMinusOne = new double[matrix.length];
        double[][] allVectors = new double[MAX_ITERATION][];

        for (int i = 0; i < y.length; i++) {
            y[i] = 1;
        }

        double abs = 0;
        for (int i = 0; i < y.length; i++) {
            abs += y[i] * y[i];
        }
        abs = Math.sqrt(abs);
        for (int i = 0; i < xKMinusOne.length; i++) {
            xKMinusOne[i] = y[i] / abs;
        }

        int k = 1;
        outer:
        for (; k <= MAX_ITERATION; k++) {
            y = Utils.multiply(matrix, xKMinusOne);

            if (k == 1) {
                allVectors[0] = Arrays.copyOf(y, y.length);
            } else {
                allVectors[k - 1] = Arrays.copyOf(Utils.multiply(matrix, allVectors[k - 2]), allVectors[k - 2].length);
            }

            abs = 0;
            for (int i = 0; i < y.length; i++) {
                abs += y[i] * y[i];
            }
            abs = Math.sqrt(abs);

            for (int i = 0; i < x.length; i++) {
                x[i] = y[i] / abs;
            }

            for (int i = 0; i < xKMinusOne.length; i++) {
                if (xKMinusOne[i] > EPS) {
                    if (Math.abs(xKMinusOne[i] - x[i]) < EPS) {
                        System.out.println("Stopped on " + k + " iteration");
                        lambda = y[i] / xKMinusOne[i];
                        System.out.println("First eigenvalue = " + lambda);
                        break outer;
                    }
                }

            }
            xKMinusOne = Arrays.copyOf(x, x.length);
        }

        System.out.print("First eigenvector = ");
        Utils.printArray(x);
        System.out.println();

        for (int j = 1; j < k - 1; j++) {
            double[] lambdasTwo = new double[matrix.length];
            for (int i = 0; i < lambdasTwo.length; i++) {
                double up = allVectors[j + 1][i] - lambda * allVectors[j][i];
                double down = allVectors[j][i] - lambda * allVectors[j - 1][i];
                lambdasTwo[i] = up / down;
            }

            System.out.printf("%2d. Second eigenvalue = ", j);
            double lambdaTwo = 0;
            for (int i = 0; i < lambdasTwo.length; i++) {
                lambdaTwo += lambdasTwo[i];
            }
            lambdaTwo /= lambdasTwo.length;
            System.out.printf("%4f", lambdaTwo);

            double[] secondEigenvector = new double[matrix.length];
            for (int i = 0; i < secondEigenvector.length; i++) {
                secondEigenvector[i] = allVectors[j + 1][i] - lambda * allVectors[j][i];
            }

            double absSecond = 0;
            for (int i = 0; i < secondEigenvector.length; i++) {
                absSecond += secondEigenvector[i] * secondEigenvector[i];
            }
            absSecond = Math.sqrt(absSecond);
            for (int i = 0; i < secondEigenvector.length; i++) {
                secondEigenvector[i] /= absSecond;
            }

            System.out.print(";  Second eigenvector = ");
            Utils.printArray(secondEigenvector, 4);
            System.out.println();
        }

        return lambda;
    }
}