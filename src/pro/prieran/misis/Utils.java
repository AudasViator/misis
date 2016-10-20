package pro.prieran.misis;

import java.util.Arrays;

public class Utils {
    private Utils() {
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] line : matrix) {
            printArray(line);
        }
    }

    public static void printArray(double[] array) {
        System.out.print("| ");
        for (double value : array) {
            System.out.printf("%6.2f ", value);
        }
        System.out.println("|");
    }

    public static void printArray(double[] array, int accuracy) {
        System.out.print("| ");
        for (double value : array) {
            System.out.printf("%6." + accuracy + "f ", value);
        }
        System.out.println("|");
    }

    public static double[][] multiply(double[][] first, double[][] second) {
        if (first[0].length != second.length) {
            throw new IllegalArgumentException();
        }

        double[][] product = new double[first.length][second[0].length];
        for (int i = 0; i < product.length; i++) {
            for (int j = 0; j < product[i].length; j++) {
                for (int k = 0; k < second.length; k++) {
                    product[i][j] += first[i][k] * second[k][j];
                }
            }
        }

        return product;
    }

    public static double[] multiply(double[][] first, double[] second) {
        if (second.length != first.length) {
            throw new IllegalArgumentException("Number of column of the first matrix is not equal " +
                    "to number of row of the second matrix");
        }

        double[] product = new double[first.length];
        for (int i = 0; i < product.length; i++) { // Строка
            for (int k = 0; k < first.length; k++) {
                product[i] += first[i][k] * second[k];
            }
        }

        return product;
    }

    public static double[][] copy(double[][] origin) {
        double[][] copy = new double[origin.length][];
        for (int i = 0; i < origin.length; i++) {
            copy[i] = Arrays.copyOf(origin[i], origin[i].length);
        }
        return copy;
    }
}
