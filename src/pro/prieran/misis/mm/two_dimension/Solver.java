package pro.prieran.misis.mm.two_dimension;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import pro.prieran.misis.Point;

import java.util.Arrays;
import java.util.List;

class Solver {

    private double[] xValues;
    private double[] tValues;

    Values solve(Function1<Double, Double> alpha, Function1<Double, Double> beta, Function2<Double, Double, Double> f, Function2<Double, Double, Double> c, Function1<Integer, Double> xSteps, Function1<Integer, Double> tSteps, int countOfXSteps, int countOfTSteps, double xFrom, double tFrom) {

        xValues = new double[countOfXSteps + 1];
        xValues[0] = xFrom;
        for (int i = 0; i < countOfXSteps; i++) {
            xValues[i + 1] += xValues[i] + xSteps.invoke(i);
        }

        tValues = new double[countOfTSteps + 1];
        tValues[0] = tFrom;
        for (int i = 0; i < countOfXSteps; i++) {
            tValues[i + 1] += tValues[i] + tSteps.invoke(i);
        }

        Values values = new Values() {
            private Point[][] values = new Point[countOfTSteps + 1][countOfXSteps + 1];

            @Override
            public double get(int xCount, int tCount) {
                Point point = values[tCount][xCount];
//                if (point != null) {
                return point.y;
//                } else {
//                    return 0;
//                }
            }

            @Override
            public List<Point> getValuesForT(int tCount) {
                return Arrays.asList(values[tCount]);
            }

            @Override
            public void set(int xCount, int tCount, double value) {
                Point point = new Point(xValues[xCount], value);
                values[tCount][xCount] = point;
            }
        };

        // Копируем начальные условия
        for (int tCount = 0; tCount < countOfTSteps + 1; tCount++) {
            values.set(0, tCount, alpha.invoke(tValues[tCount]));
        }

        for (int xCount = 0; xCount < countOfXSteps + 1; xCount++) {
            values.set(xCount, 0, beta.invoke(xValues[xCount]));
        }

        for (int tCount = 1; tCount < countOfTSteps + 1; tCount++) {
            for (int xCount = 1; xCount < countOfXSteps + 1; xCount++) {
                values.set(xCount, tCount, iterate(xCount, tCount - 1, values, f, c, xSteps, tSteps));
            }
        }

        return values;
    }

    private double iterate(int xCount, int tCount, Values y, Function2<Double, Double, Double> f, Function2<Double, Double, Double> c, Function1<Integer, Double> xSteps, Function1<Integer, Double> tSteps) {
        if (c.invoke(xValues[xCount], tValues[tCount + 1]) * tSteps.invoke(tCount) <= xSteps.invoke(xCount - 1)) {
            return toTopIterator(xCount, tCount, y, f, c, xSteps, tSteps);
        } else {
            return toRightIterator(xCount, tCount, y, f, c, xSteps, tSteps);
        }
    }

    private double toTopIterator(int x, int t, Values y, Function2<Double, Double, Double> f, Function2<Double, Double, Double> c, Function1<Integer, Double> xSteps, Function1<Integer, Double> tSteps) {
        return (f.invoke(xValues[x], tValues[t]) - (c.invoke(xValues[x], tValues[t + 1]) / xSteps.invoke(x - 1)) * (y.get(x, t) - y.get(x - 1, t))) * tSteps.invoke(t) + y.get(x, t);
    }

    private double toRightIterator(int xCount, int tCount, Values y, Function2<Double, Double, Double> f, Function2<Double, Double, Double> c, Function1<Integer, Double> xSteps, Function1<Integer, Double> tSteps) {
        return (f.invoke(xValues[xCount], tValues[tCount]) - (1 / tSteps.invoke(xCount)) * (y.get(xCount - 1, tCount + 1) - y.get(xCount - 1, tCount))) * (xSteps.invoke(xCount - 1) / c.invoke(xValues[xCount], tValues[tCount + 1])) + y.get(xCount - 1, tCount + 1);
    }
}
