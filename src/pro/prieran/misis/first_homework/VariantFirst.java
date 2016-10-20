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

        double det = 1;
        for (int i = 0; i < n; i++) {
            int k = i; // Индекс максимального по модулю элемента в строке
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[k][i])) {
                    k = j;
                }
            }

            // Если строка пустая
            if (Math.abs(a[k][i]) < EPS) {
                det = 0;
                break;
            }

            // Меняем k-ую строку с i-ой
            if (i != k) {
                double[] temp = a[k];
                a[k] = a[i];
                a[i] = temp;

                det = -det;
            }

            det *= a[i][i];
            for (int j = i + 1; j < n; j++) {
                a[i][j] /= a[i][i];
            }

            for (int j = 0; j < n; j++) {
                if (j != i && Math.abs(a[j][i]) > EPS) {
                    for (int t = i + 1; t < n; t++) {
                        a[j][t] -= a[i][t] * a[j][i];
                    }
                }
            }
        }
        return det;
    }
}