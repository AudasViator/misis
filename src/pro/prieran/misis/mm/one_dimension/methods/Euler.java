package pro.prieran.misis.mm.one_dimension.methods;

import pro.prieran.misis.mm.one_dimension.interfaces.Function;
import pro.prieran.misis.mm.one_dimension.interfaces.Iterator;

public class Euler implements Iterator {
    @Override
    public double iterate(double xPrev, double yPrev, double step, Function function) {
        return step * function.solve(xPrev, yPrev);
    }
}