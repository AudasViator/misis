package pro.prieran.misis.chm.first_homework;

import pro.prieran.misis.chm.Utils;

import java.util.Arrays;

public class VariantNinth {
    public static void main(String[] args) {
//        double[][] matrix = {{2, 1}, {6, 1}}; //4, -1
        double[][] matrix = {{5, 6, 3}, {-1, 0, 1}, {1, 2, -1}};  // 4, 2, -2
//        double[][] matrix = {{17, 6}, {6, 8}};  // 20, 5
//        double[][] matrix = {{4, 1, 0}, {1, 2, 1}, {0, 1, 1}}; // 4.46, 2.24, 0.30

        System.out.print("Eigenvalues: ");
        double[] eigenvalues = eigenvaluesLu(matrix);
        Utils.printArray(eigenvalues);
        System.out.println();

        for (int i = 0; i < eigenvalues.length; i++) {
            double[][] copyMatrix = Utils.copy(matrix);
            for (int j = 0; j < copyMatrix.length; j++) {
                copyMatrix[j][j] -= eigenvalues[i];
            }
            double[][] invertedMatrix = invert(copyMatrix);

            double[] eigenvector = reverseIterationWithShift(invertedMatrix);
            System.out.printf("Eigenvector for %5.2f = ", eigenvalues[i]);
            Utils.printArray(eigenvector);
            System.out.println();
        }
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

    private synchronized static double[] reverseIterationWithShift(double[][] matrix) {
        final int MAX_ITERATION = 100;
        final double EPS = 1E-4;
        double[] y = new double[matrix.length];
        double[] x = new double[matrix.length];
        double[] xKMinusOne = new double[matrix.length];
        double[][] allVectors = new double[MAX_ITERATION][];

        for (int i = 0; i < y.length; i++) {
            y[i] = 1.0 / 3.0;
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
                        break outer;
                    }
                }

            }
            xKMinusOne = Arrays.copyOf(x, x.length);
        }

        return x;
    }

    private static double[] eigenvaluesLu(double[][] matrix) {
        double[][] upperMatrix = new double[matrix.length][matrix.length];
        double[][] lowerMatrix = new double[matrix.length][matrix.length];

        for (int i = 0; i < 20; i++) {
            lu(matrix, lowerMatrix, upperMatrix);
            matrix = Utils.multiply(upperMatrix, lowerMatrix);
        }

        double[] answer = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            answer[i] = matrix[i][i];
        }

        return answer;
    }

    private static void lu(double[][] matrix, double[][] lowerMatrix, double[][] upperMatrix) {
        for (int g = 0; g < matrix.length; g++) {
            for (int j1 = 0; j1 < matrix[g].length; j1++) {
                upperMatrix[g][j1] = matrix[g][j1];
                for (int k = 0; k < g; k++) {
                    upperMatrix[g][j1] -= lowerMatrix[g][k] * upperMatrix[k][j1];
                }
            }

            for (int j2 = 0; j2 < matrix[g].length; j2++) {
                lowerMatrix[j2][g] = matrix[j2][g];
                for (int k = 0; k < g; k++) {
                    lowerMatrix[j2][g] -= lowerMatrix[j2][k] * upperMatrix[k][g];
                }
                lowerMatrix[j2][g] /= upperMatrix[g][g];
            }
        }
    }
}