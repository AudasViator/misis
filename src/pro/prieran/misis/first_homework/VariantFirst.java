package pro.prieran.misis.first_homework;

import pro.prieran.misis.Utils;

public class VariantFirst {
    private static final double EPS = 1E-9;

    public static void main(String[] args) {
        double[][] coefficients = {{10, -2, -2}, {-1, 10, -2}, {-1, -1, 10}};
        double[] constants = {12, 14, 16};
        double[] approximation = new double[coefficients.length];

        for (int i = 0; i < 40; i++) {
            relaxation(approximation, coefficients, constants, 0.5);
        }

        System.out.print("Relax : ");
        Utils.printArray(approximation);

        System.out.print("Cramer: ");
        Utils.printArray(cramer(coefficients, constants));
    }

    private static void relaxation(double[] approximation, double[][] coefficients, double[] constants, double parameter) {
        int n = coefficients.length;
        double sum1, sum2;
        for (int i = 0; i < n; i++) {
            sum1 = 0;
            sum2 = 0;
            for (int j = 0; j < i; j++) {
                // Нижнетреугольная
                sum1 += -coefficients[i][j] * parameter / coefficients[i][i] * approximation[j];
            }
            for (int j = i + 1; j < n; j++) {
                // Верхнетреугольная
                sum2 += -coefficients[i][j] * parameter / coefficients[i][i] * approximation[j];
            }
            approximation[i]
                    = sum1 + sum2
                    + constants[i] * parameter / coefficients[i][i]
                    - approximation[i] * (parameter - 1);
        }
    }

    private static double[] cramer(double[][] coefficients, double[] constants) {
        double[] answer = new double[coefficients.length];
        double generalDet = det(coefficients);
        for (int i = 0; i < constants.length; i++) {
            double[][] anotherCoefficients = Utils.copy(coefficients);
            for (int j = 0; j < anotherCoefficients.length; j++) {
                anotherCoefficients[j][i] = constants[j];
            }
            answer[i] = det(anotherCoefficients) / generalDet;
        }
        return answer;
    }

    private static double det(double[][] matrix) {
        double[][] a = Utils.copy(matrix);
        int n = a.length;
        double[][] t = new double[n][n];

        for (int k = 0; k < n - 1; k++) {
            for (int i = k + 1; i < n; i++) {
                t[i][k] = a[i][k] / a[k][k];

                for (int j = k + 1; j < n; j++) {
                    a[i][j] -= t[i][k] * a[k][j];
                }
            }
        }

        double det = a[0][0];
        for (int k = 1; k < n; k++) {
            det *= a[k][k];
        }

        return det;
    }
}