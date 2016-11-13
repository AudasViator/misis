package pro.prieran.misis;

import javafx.beans.property.SimpleDoubleProperty;
import org.jetbrains.annotations.NotNull;

public class Point implements Comparable<Point> {
    private final SimpleDoubleProperty xProperty;
    private final SimpleDoubleProperty yProperty;
    private final SimpleDoubleProperty yAprProperty;
    private final SimpleDoubleProperty deltaProperty;
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        xProperty = new SimpleDoubleProperty(x);
        yProperty = new SimpleDoubleProperty(y);
        yAprProperty = new SimpleDoubleProperty();
        deltaProperty = new SimpleDoubleProperty();
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int compareTo(@NotNull Point o) {
        return (int) (x - o.x);
    }

    public double getxProperty() {
        return xProperty.get();
    }

    public SimpleDoubleProperty xProperty() {
        return xProperty;
    }

    public double getyProperty() {
        return yProperty.get();
    }

    public SimpleDoubleProperty yProperty() {
        return yProperty;
    }

    public double getyAprProperty() {
        return yAprProperty.get();
    }

    public SimpleDoubleProperty yAprProperty() {
        return yAprProperty;
    }

    public double getDeltaProperty() {
        return deltaProperty.get();
    }

    public SimpleDoubleProperty deltaProperty() {
        return deltaProperty;
    }
}
