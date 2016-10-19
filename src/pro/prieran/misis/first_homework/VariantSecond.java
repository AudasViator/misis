package pro.prieran.misis.first_homework;

import pro.prieran.misis.Utils;

public class VariantSecond {
    public static void main(String[] args) {
        double[][] matrix = {{5, 6, 3}, {-1, 0, 1}, {1, 2, -1}};  // 4, 2, -2

        System.out.println("Invert matrix:");
        Utils.printMatrix(invert(matrix));

        System.out.println();

        iteration(matrix);
    }


    private static double[][] invert(double matrix[][]) {
        double[][] a = Utils.copy(matrix);
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i = 0; i < n; ++i)
            b[i][i] = 1;

        // Преобразование в верхнетреугольную
        gaussian(a, index);

        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }

        for (int i = 0; i < n; ++i) {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for (int j = n - 2; j >= 0; --j) {
                x[j][i] = b[index[j]][i];
                for (int k = j + 1; k < n; ++k) {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    private static void gaussian(double a[][], int index[]) {
        int n = index.length;
        double c[] = new double[n];

        for (int i = 0; i < n; ++i) {
            index[i] = i;
        }

        for (int i = 0; i < n; ++i) {
            double c1 = 0;
            for (int j = 0; j < n; ++j) {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        int k = 0;
        for (int j = 0; j < n - 1; ++j) {
            double pi1 = 0;
            for (int i = j; i < n; ++i) {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) {
                    pi1 = pi0;
                    k = i;
                }
            }

            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i = j + 1; i < n; ++i) {
                double pj = a[index[i]][j] / a[index[j]][j];
                a[index[i]][j] = pj;
                for (int l = j + 1; l < n; ++l) {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
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
