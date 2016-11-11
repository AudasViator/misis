package pro.prieran.misis.mm.one_dimension.methods;

import pro.prieran.misis.mm.one_dimension.interfaces.Function;
import pro.prieran.misis.mm.one_dimension.interfaces.Iterator;

public class RungeKutta implements Iterator {

    @Override
    public double iterate(double xPrev, double yPrev, double step, Function function) {
        double k1 = function.solve(xPrev, yPrev);
        double k2 = function.solve(xPrev + step / 2, yPrev + step * k1 / 2);
        double k3 = function.solve(xPrev + step / 2, yPrev + step * k2 / 2);
        double k4 = function.solve(xPrev + step, yPrev + step * k3);

        return step * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }
}
