package pro.prieran.misis.chm.second_homework.brown;

import pro.prieran.misis.chm.Utils;

public class Brown {

    public static void main(String[] args) {

        double[] arr = {1, 1, 1};
        for (int i = 0; i < 1000; i++) {
            arr = iterate(arr[0], arr[1], arr[2]);
            System.out.println();
            Utils.printArray(arr);
        }
    }

    private static double[] iterate(double x, double y, double z) {
        double tempX = x - (f(x, y, z)) / (fX(x, y, z));
        double tempY = y - (g(x, y, z)) / (gY(x, y, z));

        double newZ = z - u(tempX, tempY, z) * Math.pow(uZ(tempX, tempY, z) - uX(tempX, tempY, z) * fZ(tempX, tempY, z) / fX(tempX, tempY, z) - uY(tempX, tempY, z) * gZ(tempX, tempY, z) / gY(tempX, tempY, z), -1);

        double newY = y - g(tempX, tempY, z) / gY(tempX, tempY, z) - gZ(tempX, tempY, z) / gY(tempX, tempY, z) * (newZ - z);

        double newX = x - f(tempX, tempY, z) / fX(tempX, tempY, z) - fY(tempX, tempY, z) / fX(tempX, tempY, z) * (newY - y) - fZ(tempX, tempY, z) / fX(tempX, tempY, z) * (newZ - z);

        return new double[]{newX, newY, newZ};
    }

    private static double f(double x, double y, double z) {
        return Math.pow(x, 2) + Math.pow(y, 3) - 10;
    }

    private static double fX(double x, double y, double z) {
        return 2 * x;
    }

    private static double fY(double x, double y, double z) {
        return 3 * Math.pow(y, 2);
    }

    private static double fZ(double x, double y, double z) {
        return 0;
    }

    private static double g(double x, double y, double z) {
        return Math.pow(x, 3) - z + Math.pow(y, 3);
    }

    private static double gY(double x, double y, double z) {
        return 3 * Math.pow(y, 2);
    }

    private static double gZ(double x, double y, double z) {
        return -1;
    }

    private static double u(double x, double y, double z) {
        return Math.pow(x, 3) + z - 10;
    }

    private static double uX(double x, double y, double z) {
        return 3 * Math.pow(x, 2);
    }

    private static double uY(double x, double y, double z) {
        return 0;
    }

    private static double uZ(double x, double y, double z) {
        return 1;
    }
}
