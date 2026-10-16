package w2d4;

public class Triangle extends Shape {
    private final double base;
    private final double height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public double area() {
        return base * height / 2;
    }

    @Override
    public String getName() {
        return "Triangle";
    }

    @Override
    public String toString() {
        return "Triangle[b=" + base + ", h=" + height + ", area=" + String.format("%.2f", area()) + "]";
    }
}

//        - `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` →
//        hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
//        - Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için
//        `String.format("%.2f", x)`).
//