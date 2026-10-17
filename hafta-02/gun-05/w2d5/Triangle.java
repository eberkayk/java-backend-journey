package w2d5;

public class Triangle extends Shape {
    private final double base;
    private final double height;

    public Triangle(double base, double height) {
        super();
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

}
//
//2. `Triangle`'dan `area()` metodunu **geçici olarak** yoruma al → derle → hata mesajını kağıda kopyala. Mesajda hangi class'ın, hangi metodun adı geçiyor? Geri aç.