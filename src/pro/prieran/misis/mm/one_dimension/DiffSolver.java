package pro.prieran.misis.mm.one_dimension;

import com.sun.istack.internal.NotNull;
import kotlin.jvm.functions.Function2;
import pro.prieran.misis.Point;

import java.util.ArrayList;
import java.util.List;

class DiffSolver {

    @NotNull
    List<Point> solve(@NotNull Function2<Double, Double, Double> function, @NotNull Iterator iterator, double y0, double x0, double step, double fromX, double toX) {
        if (fromX > toX || x0 > fromX || step <= 0) {
            throw new IllegalArgumentException();
        }

        int numberOfIteration = (int) ((toX - fromX) / step);
        List<Point> points = new ArrayList<>(numberOfIteration);

        double y = y0;
        double yPrev = y0;
        double x = x0;
        double xPrev = x0;

        if (x0 >= fromX) {
            points.add(new Point(x0, y0));
        }

        while (x < toX) {
            y += iterator.iterate(xPrev, yPrev, step, function);
            x = xPrev + step;

            if (x > fromX) {
                points.add(new Point(x, y));
            }
            xPrev = x;
            yPrev = y;
        }

        return points;
    }
}
