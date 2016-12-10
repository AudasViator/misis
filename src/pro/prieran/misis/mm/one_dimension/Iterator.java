package pro.prieran.misis.mm.one_dimension;

import kotlin.jvm.functions.Function2;

interface Iterator {
    double iterate(double xPrev, double yPrev, double step, Function2<Double, Double, Double> function);
}