package pro.prieran.misis.mm.interfaces;

public interface Iterator {
    double iterate(double xPrev, double yPrev, double step, Function function);
}