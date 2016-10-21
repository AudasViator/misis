package pro.prieran.misis.mm;

import pro.prieran.misis.mm.methods.Euler;
import pro.prieran.misis.mm.methods.RungeKutta;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final double step = 1E-4;

        DiffSolver solver = new DiffSolver();
        Euler euler = new Euler();
        RungeKutta rungeKutta = new RungeKutta();

        List<Point> eulerPoints = solver.solve(Main::test, euler, 0, 0, step, 0, 2);
        List<Point> kuttaPoints = solver.solve(Main::test, rungeKutta, 0, 0, step, 0, 2);

        for (int i = 0; i < eulerPoints.size() && i < kuttaPoints.size(); i++) {
            Point eiler = eulerPoints.get(i);
            Point kutta = kuttaPoints.get(i);
//            System.out.printf(Locale.US, "i=%6d, x = %6.4f, EilerY = %6.6f, kuttaY = %6.6f", i, eiler.x, eiler.y, kutta.y);
//            System.out.printf(Locale.US, ", diff=%6.6f\n", (eiler.y - kutta.y));
            double truue = Math.exp(Math.cos(2 * eiler.x) - eiler.x / 2) * (2 * Math.sin(2 * eiler.x) - 1 / 2);
            System.out.printf("diffEuler=%.8f", (truue - eiler.y));
            System.out.printf(", diffKutta=%.8f", (truue - kutta.y));
            System.out.printf(", diffDiff=%.8f\n", ((truue - eiler.y) - (truue - kutta.y)));
        }
    }

    private static double test(double x, double y) {
        return Math.exp(Math.cos(2 * x) - x / 2);
    }
}
