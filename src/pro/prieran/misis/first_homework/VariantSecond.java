package pro.prieran.misis.first_homework;

import pro.prieran.misis.Utils;

public class VariantSecond {
    private static final double EPS = 1E-9;

    public static void main(String[] args) {
//        double[][] matrix = {{5, 6, 3}, {-1, 0, 1}, {1, 2, -1}};  // 4, 2, -2
//        double[][] matrix = {{3, 2, -5}, {2, -1, 3}, {1, 2, -1}};
//        double[][] matrix = {{1, 2, 4}, {5, 1, 2}, {3, -1, 1}};
        double[][] matrix = {{2, 3, 2, 2}, {-1, -1, 0, -1}, {-2, -2, -2, -1}, {3, 2, 2, 2}};

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
        final double EPS = 0.001;
        double[] yKMinusTwo = new double[matrix.length];
        double[] yKMinusOne = new double[matrix.length];
        double[] y = new double[matrix.length];
        double[] x = new double[matrix.length];
        double[] xKMinusOne = new double[matrix.length];
        double[] lambdas = new double[matrix.length];
        for (int i = 0; i < y.length; i++) {
            y[i] = 0.33;
        }

        double abs = 0;
        for (int i = 0; i < y.length; i++) {
            abs += y[i] * y[i];
        }
        abs = Math.sqrt(abs);
        for (int i = 0; i < xKMinusOne.length; i++) {
            xKMinusOne[i] = y[i] / abs;
        }

        int numberOfIterations = 40;
        for (int k = 1; k <= numberOfIterations; k++) {
            y = Utils.multiply(matrix, xKMinusOne);

            for (int i = 0; i < y.length; i++) {
                abs += y[i] * y[i];
            }
            abs = Math.sqrt(abs);

            for (int i = 0; i < x.length; i++) {
                x[i] = y[i] / abs;
            }

            for (int i = 0; i < lambdas.length; i++) {
                if (xKMinusOne[i] > EPS) {
                    lambdas[i] = y[i] / xKMinusOne[i];
                }
            }
            xKMinusOne = x;

            if (k == numberOfIterations - 2) {
                yKMinusTwo = y;
            }
            if (k == numberOfIterations - 1) {
                yKMinusOne = y;
            }
        }

        double lambda = 0;
        for (int i = 0; i < lambdas.length; i++) {
            lambda += lambdas[i];
        }
        lambda /= lambdas.length;

        double[] lambdasTwo = new double[y.length];
        for (int i = 0; i < lambdasTwo.length; i++) {
            lambdasTwo[i] = (y[i] - lambda * yKMinusOne[i])
                    / (yKMinusOne[i] - lambda * yKMinusTwo[i]);
        }

        double lambdaTwo = 0;
        for (int i = 0; i < lambdasTwo.length; i++) {
            lambdaTwo += lambdasTwo[i];
        }
        lambdaTwo /= lambdasTwo.length;

        System.out.printf("First eigenvalue: %.2f\n", lambda);
        System.out.print("First eigenvector: ");
        Utils.printArray(x);

        System.out.printf("Second eigenvalue: %.2f\n", lambdaTwo);
        return lambda;
    }
}
