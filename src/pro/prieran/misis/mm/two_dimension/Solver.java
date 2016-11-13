package pro.prieran.misis.mm.two_dimension;

import pro.prieran.misis.Point;
import pro.prieran.misis.mm.two_dimension.interfaces.Func1;
import pro.prieran.misis.mm.two_dimension.interfaces.Func2;
import pro.prieran.misis.mm.two_dimension.interfaces.Values;

import java.util.Arrays;
import java.util.List;

public class Solver {
    public Values solve(Func1 alpha, Func1 beta, Func2 f, Func2 c, Func1 xSteps, Func1 tSteps, int countOfXSteps, int countOfTSteps, double xFrom) {
        // alpha(t), beta(x)
        // values[x][t]

        final double[] xValues = new double[countOfXSteps + 1];
        xValues[0] = xFrom;
        for (int i = 0; i < countOfXSteps; i++) {
            xValues[i + 1] += xValues[i] + xSteps.get(i);
        }

        Values values = new Values() {
            private Point[][] values = new Point[countOfTSteps + 1][countOfXSteps + 1];

            @Override
            public double get(int x, int t) {
                Point point = values[t][x];
                if (point != null) {
                    return point.y;
                } else {
                    return 0;
                }
            }

            @Override
            public List<Point> getValuesForT(int t) {
                return Arrays.asList(values[t]);
            }

            @Override
            public void set(int x, int t, double value) {
                Point point = new Point(xValues[x], value);
                values[t][x] = point;
            }

            @Override
            public int sizeX() {
                return countOfXSteps;
            }

            @Override
            public int sizeT() {
                return countOfTSteps;
            }
        };

        // Копируем начальные условия
        for (int t = 0; t < countOfTSteps; t++) {
            values.set(0, t, alpha.get(t));
        }
        for (int x = 0; x < countOfXSteps; x++) {
            values.set(x, 0, beta.get(x));
        }

        for (int t = 0; t < countOfTSteps; t++) {
            for (int x = 1; x < countOfXSteps; x++) {
                values.set(x, t, iterate(x, t, values, f, c, xSteps, tSteps));
            }
        }

        return values;
    }

    private double iterate(int x, int t, Values y, Func2 f, Func2 c, Func1 xSteps, Func1 tSteps) {
        if (c.get(x, t + 1) <= xSteps.get(x - 1)) {
            return toTopIterator(x, t, y, f, c, xSteps, tSteps);
        } else {
            return toRightIterator(x, t, y, f, c, xSteps, tSteps);
        }
    }

    private double toRightIterator(int x, int t, Values y, Func2 f, Func2 c, Func1 xSteps, Func1 tSteps) {
        return (f.get(x, t) - (1 / tSteps.get(x)) * (y.get(x - 1, t + 1) - y.get(x - 1, t))) * (xSteps.get(x - 1) / c.get(x, t + 1)) + y.get(x - 1, t + 1);
    }

    private double toTopIterator(int x, int t, Values y, Func2 f, Func2 c, Func1 xSteps, Func1 tSteps) {
        return (f.get(x, t) - (c.get(x, t + 1) / xSteps.get(x - 1)) * (y.get(x, t) - y.get(x + 1, t))) * tSteps.get(t) + y.get(x, t);
    }
}
