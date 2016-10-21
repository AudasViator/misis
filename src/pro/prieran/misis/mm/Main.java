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

        List<Point> eulerPoints = solver.solve(Main::test, euler, 1, 2, step, 11, 12);
        List<Point> kuttaPoints = solver.solve(Main::test, rungeKutta, 1, 2, step, 11, 12);

        for (int i = 0; i < eulerPoints.size() && i < kuttaPoints.size(); i++) {
            Point eiler = eulerPoints.get(i);
            Point kutta = eulerPoints.get(i);
//            System.out.println("x = " + eiler.x + ", EilerY = " + eiler.y + ", kuttaY = " + kutta.y);
            System.out.println("i=" + i + ", diff=" + (eiler.y - kutta.y));
        }
    }

    private static double test(double x, double y) {
        return Math.sin(x * y) / Math.sqrt(x * y);
    }
}
