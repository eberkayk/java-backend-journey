package w2d5;

public class Circle extends Shape implements Describable {
    private final double radius;

    public Circle(double radius){
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
    public String describe() {
        return "Circle is a shape its area is: " + String.format("%.2f", area());
    }
}

//        - `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` →
//        hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
//        - Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için
//        `String.format("%.2f", x)`).
//