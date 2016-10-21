package pro.prieran.misis.chm.first_homework;

import pro.prieran.misis.chm.Utils;


public class VariantNinth {
    public static void main(String[] args) {
//        double[][] matrix = {{2, 1}, {6, 1}}; //4, -1
        double[][] matrix = {{5, 6, 3}, {-1, 0, 1}, {1, 2, -1}};  // 4, 2, -2
//        double[][] matrix = {{0, 2}, {3, 5}};  // 6, -1
//        double[][] matrix = {{17, 6}, {6, 8}};  // 20, 5

        System.out.print("Eigenvalues: ");
        double[] eigenvalues = eigenvaluesLu(matrix);
        Utils.printArray(eigenvalues);

        for (int i = 0; i < eigenvalues.length; i++) {
            matrix[i][i] -= eigenvalues[0];
        }
        Utils.printMatrix(matrix);

        gauss(matrix);
    }

    private static void gauss(double[][] matrix) {
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

        System.out.println("t");
        Utils.printMatrix(t);
        System.out.println("a");
        Utils.printMatrix(a);
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