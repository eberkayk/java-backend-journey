package w2d4;

public class Shape {
    Shape(){

    }

    public double area() {
        return 0;
    }

    public String getName() {
        return "Shape";
    }
}

//### E3 — Şekiller ⭐⭐
//        `w2d4/Shape.java`, `Circle.java`, `Rectangle.java`, `Triangle.java`:
//        - `Shape`: `public double area()` → `0` döndürür. `public String getName()` → `"Shape"`.
//        - `Circle(double radius)`, `Rectangle(double width, double height)`, `Triangle(double base, double height)` → hepsi `extends Shape`, `area()` ve `getName()`'i **override** eder. Field'lar `private final`.
//        - Her birinde `toString()` override: ör. `Circle[r=2.0, area=12.57]` (yuvarlamak için `String.format("%.2f", x)`).
//
//        `w2d4/ShapeDemo.java`:
//        - 5 elemanlı `Shape[]` (en az 1'er tane her tür), hepsini **direkt `println`** ile yazdır.
//        - `static Shape largest(Shape[] shapes)` ve `static double totalArea(Shape[] shapes)`.
//        - **Tahmin sorusu — bilmemen normal (yarının konusu):** `new Shape()` yazıp `area()`'sını çağırmak **mantıklı mı**? "Şekil" diye bir nesnenin alanı 0 mı? Bir alt class `area()`'yı override etmeyi **unutursa** ne olur — derleyici seni uyarır mı? Kağıda 2-3 cümle. (İpucu: Gün 5'te `abstract`.)
