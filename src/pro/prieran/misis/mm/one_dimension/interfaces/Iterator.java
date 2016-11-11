package pro.prieran.misis.mm.one_dimension.interfaces;

public interface Iterator {
    double iterate(double xPrev, double yPrev, double step, Function function);
}