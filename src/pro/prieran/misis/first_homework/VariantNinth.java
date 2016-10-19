package pro.prieran.misis.first_homework;

import pro.prieran.misis.Utils;

public class VariantNinth {
    public static void main(String[] args) {
        double[][] matrix = {{2, 1}, {6, 1}}; //4, -1

        System.out.print("Eigenvalues: ");
        Utils.printArray(eigenvaluesLu(matrix));
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