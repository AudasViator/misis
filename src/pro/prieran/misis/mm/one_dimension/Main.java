package pro.prieran.misis.mm.one_dimension;

import kotlin.jvm.functions.Function2;
import pro.prieran.misis.Point;

import java.util.List;

import static java.lang.Math.*;

public class Main {

    private static final double STEP = 1E-4;
    private static final Function2<Double, Double, Double> TO_APPROX =
            (x, y) -> exp(cos(2 * x) - x / 2) * (-2 * sin(2 * x) - 0.5)
                    + sin(5 * x) * exp(cos(2 * x) - x / 2) - sin(5 * x) * exp(cos(2 * x) - x / 2);

    public static void main(String[] args) {

        DiffSolver solver = new DiffSolver();
        Euler euler = new Euler();
        RungeKutta rungeKutta = new RungeKutta();

        List<Point> eulerPoints = solver.solve(TO_APPROX, euler, exp(1), 0, STEP, 1, 4);
        List<Point> kuttaPoints = solver.solve(TO_APPROX, rungeKutta, exp(1), 0, STEP, 1, 4);

        for (int i = 0; i < eulerPoints.size() && i < kuttaPoints.size(); i++) {
            Point eiler = eulerPoints.get(i);
            Point kutta = kuttaPoints.get(i);
            double truue = exp(cos(2 * eiler.x) - eiler.x / 2); // Аналитическое решение
            System.out.printf("diffEuler=%.8f", (truue - eiler.y)); // Аналитческое vs Эйлер
            System.out.printf(", diffKutta=%.20f", (truue - kutta.y)); // Аналитическое vs Рунге-Кутта
            System.out.printf(", diffDiff=%.8f\n", (eiler.y - kutta.y)); // Эйлер vs Рунге-Кутта
        }
    }
}