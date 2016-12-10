package pro.prieran.misis.mm.two_dimension;

import pro.prieran.misis.Point;

import java.util.List;

interface Values {
    double get(int x, int t);

    List<Point> getValuesForT(int t);

    void set(int x, int t, double value);
}
