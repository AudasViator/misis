package pro.prieran.misis.mm.methods;

import pro.prieran.misis.mm.interfaces.Function;
import pro.prieran.misis.mm.interfaces.Iterator;

public class Euler implements Iterator {
    @Override
    public double iterate(double xPrev, double yPrev, double step, Function function) {
        return step * function.solve(xPrev, yPrev);
    }
}