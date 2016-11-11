package pro.prieran.misis.mm.one_dimension;

import pro.prieran.misis.mm.Point;
import pro.prieran.misis.mm.one_dimension.methods.Euler;
import pro.prieran.misis.mm.one_dimension.methods.RungeKutta;

import java.util.List;

import static java.lang.Math.*;

public class Main {
    public static void main(String[] args) {
        final double step = 1E-4;

        DiffSolver solver = new DiffSolver();
        Euler euler = new Euler();
        RungeKutta rungeKutta = new RungeKutta();

        List<Point> eulerPoints = solver.solve(Main::func, euler, exp(1), 0, step, 1, 4);
        List<Point> kuttaPoints = solver.solve(Main::func, rungeKutta, exp(1), 0, step, 1, 4);

        for (int i = 0; i < eulerPoints.size() && i < kuttaPoints.size(); i++) {
            Point eiler = eulerPoints.get(i);
            Point kutta = kuttaPoints.get(i);
//            System.out.printf(Locale.US, "i=%6d, x = %6.4f, EilerY = %6.6f, kuttaY = %6.6f", i, eiler.x, eiler.y, kutta.y);
//            System.out.printf(Locale.US, ", diff=%6.6f\n", (eiler.y - kutta.y));
            double truue = exp(cos(2 * eiler.x) - eiler.x / 2);
            System.out.printf("diffEuler=%.8f", (truue - eiler.y));
            System.out.printf(", diffKutta=%.20f", (truue - kutta.y));
            System.out.printf(", diffDiff=%.8f\n", ((truue - eiler.y) - (truue - kutta.y)));
        }
    }

    private static double func(double x, double y) {
        return exp(cos(2 * x) - x / 2) * (-2 * sin(2 * x) - 0.5)
                + sin(5 * x) * exp(cos(2 * x) - x / 2) - sin(5 * x) * exp(cos(2 * x) - x / 2);
    }
}