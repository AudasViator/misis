package pro.prieran.misis.mm.one_dimension;

import kotlin.jvm.functions.Function2;

class RungeKutta implements Iterator {

    @Override
    public double iterate(double xPrev, double yPrev, double step, Function2<Double, Double, Double> function) {
        double k1 = function.invoke(xPrev, yPrev);
        double k2 = function.invoke(xPrev + step / 2, yPrev + step * k1 / 2);
        double k3 = function.invoke(xPrev + step / 2, yPrev + step * k2 / 2);
        double k4 = function.invoke(xPrev + step, yPrev + step * k3);

        return step * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }
}
