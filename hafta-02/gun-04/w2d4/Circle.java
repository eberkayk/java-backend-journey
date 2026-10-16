package w2d4;

public class Circle extends Shape {
    private final double radius;

    Circle(double radius){
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String getName() {
        return "Circle";
    }

    @Override
    public String toString() {
        return "Circle[r=" + radius + ", area=" + String.format("%.2f", area()) + "]";
    }
}

//        - `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` →
//        hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
//        - Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için
//        `String.format("%.2f", x)`).
//