package w2d5;

public abstract class Shape {
    public Shape() {}
    public abstract double area();

    public abstract String getName();

    @Override
    public String toString() {
        return getName() + "[area=" + String.format("%.2f", area()) + "]";
    }
}