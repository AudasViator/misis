package pro.prieran.misis.mm.one_dimension;

import kotlin.jvm.functions.Function2;

class Euler implements Iterator {
    @Override
    public double iterate(double xPrev, double yPrev, double step, Function2<Double, Double, Double> function) {
        return step * function.invoke(xPrev, yPrev);
    }
}