package pro.prieran.misis.first_homework;

import pro.prieran.misis.Utils;

import java.util.Arrays;

public class VariantFirst {
    private static final double EPS = 1E-9;
    private static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        double[][] matrix = {{10, -2, -2}, {-1, 10, -2}, {-1, -1, 10}};
        double[] rightSide = {12, 14, 16};
//        double[][] matrix = {{7, 6, 3}, {-1, 2, 1}, {1, 2, 1},};
//        double[] rightSide = {0, 0, 0};
        double[] approximation = new double[matrix.length];

        for (int i = 0; i < 40; i++) {
            relaxation(approximation, matrix, rightSide, 0.5);
        }

        double[] answers = new double[rightSide.length];
        gauss(matrix, rightSide, answers);

        System.out.print("Relax : ");
        Utils.printArray(approximation);

        System.out.print("Cramer: ");
        Utils.printArray(cramer(matrix, rightSide));

        System.out.print("Gauss : ");
        Utils.printArray(answers);
    }

    private static void relaxation(double[] approximation, double[][] coefficients, double[] constants, double parameter) {
        int n = coefficients.length;
        double s1, s2;
        for (int i = 0; i < n; i++) {
            s1 = 0;
            s2 = 0;
            for (int j = 0; j < i; j++) {
                // Нижнетреугольная
                s1 += -coefficients[i][j] * parameter / coefficients[i][i] * approximation[j];
            }
            for (int j = i + 1; j < n; j++) {
                // Верхнетреугольная
                s2 += -coefficients[i][j] * parameter / coefficients[i][i] * approximation[j];
            }
            approximation[i]
                    = s1 + s2
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
            int k = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[k][i])) {
                    k = j;
                }
            }

            if (Math.abs(a[k][i]) < EPS) {
                det = 0;
                break;
            }

            double[] temp = a[k];
            a[k] = a[i];
            a[i] = temp;

            if (i != k) {
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

    private static int gauss(double[][] matrix, double[] constants, double[] ans) {
        double[][] a = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            a[i] = new double[matrix[i].length + 1];
            System.arraycopy(matrix[i], 0, a[i], 0, matrix[i].length);
            a[i][matrix[i].length] = constants[i];
        }

        int n = a.length;
        int m = a[0].length - 1;

        int[] where = new int[m];
        for (int i = 0; i < where.length; i++) {
            where[i] = -1;
        }
        for (int col = 0, row = 0; col < m && row < n; ++col) {
            int sel = row;
            for (int i = row; i < n; ++i)
                if (Math.abs(a[i][col]) > Math.abs(a[sel][col]))
                    sel = i;
            if (Math.abs(a[sel][col]) < EPS)
                continue;
            for (int i = col; i <= m; ++i) {
                double temp = a[row][i];
                a[row][i] = a[sel][i];
                a[sel][i] = temp;
            }
            where[col] = row;

            for (int i = 0; i < n; ++i)
                if (i != row) {
                    double c = a[i][col] / a[row][col];
                    for (int j = col; j <= m; ++j)
                        a[i][j] -= a[row][j] * c;
                }
            ++row;
        }

        Arrays.fill(ans, 0);

        for (int i = 0; i < a.length; i++) {
            double max = 0;
            for (int j = 0; j < a[i].length; j++) {
                if (Math.abs(a[i][j]) > Math.abs(max)) {
                    max = Math.abs(a[i][j]);
                }
            }
            if (max != 0) {
                for (int j = 0; j < a[i].length; j++) {
                    a[i][j] /= max;
                }
            }
        }

        System.out.println();
        Utils.printMatrix(a);
        System.out.println();

        for (int i = 0; i < m; ++i)
            if (where[i] != -1)
                ans[i] = a[where[i]][m] / a[where[i]][i];
        for (int i = 0; i < n; ++i) {
            double sum = 0;
            for (int j = 0; j < m; ++j)
                sum += ans[j] * a[i][j];
            if (Math.abs(sum - a[i][m]) > EPS)
                return 0;
        }

        for (int i = 0; i < m; ++i)
            if (where[i] == -1) {
                return INF;
            }
        return 1;
    }
}