package w2d5;

public class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
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

}

//        - `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` →
//        hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
//        - Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için
//        `String.format("%.2f", x)`).
//