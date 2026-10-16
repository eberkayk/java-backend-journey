package w2d4;

public class Rectangle extends Shape {
    private final double width;
    private final double height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String getName() {
        return "Rectangle";
    }

    @Override
    public String toString() {
        return "Rectangle[w=" + width + ", h=" + height + ", area=" + String.format("%.2f", area()) + "]";
    }
}

//        - `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` →
//        hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
//        - Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için
//        `String.format("%.2f", x)`).
//