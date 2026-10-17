package w2d5;


public class ShapeDemo {
    public static void main(String[] args) {
        Shape[] shapes = new Shape[5];
        shapes[0] = new Circle(5);
        shapes[1] = new Rectangle(3, 5);
        shapes[2] = new Triangle(6, 3);
        shapes[3] = new Rectangle(2.3, 6.7);
        shapes[4] = new Triangle(7.21, 8.16);

        for (Shape shape : shapes) {
            System.out.println(shape);
        }

        System.out.println(largest(shapes));
        System.out.println(String.format("%.2f", totalArea(shapes)));
    }

    public static Shape largest(Shape[] shapes) {
        Shape largest = shapes[0];
        for (int i = 1; i < shapes.length; i++) {
            if (largest.area() < shapes[i].area()) {
                largest = shapes[i];
            }
        }
        return largest;
    }

    public static double totalArea(Shape[] shapes) {
        double sum = 0;
        for (int i = 0; i < shapes.length; i++) {
            sum += shapes[i].area();
        }
        return sum;
    }
}

//        `w2d4/ShapeDemo.java`:
//        - 5 elemanlı `Shape[]` (en az 1'er tane her tür), hepsini **direkt `println`** ile yazdır.
//        - `static Shape largest(Shape[] shapes)` ve `static double totalArea(Shape[] shapes)`.
//        - **Tahmin sorusu — bilmemen normal (yarının konusu):** `new Shape()` yazıp `area()`'sını çağırmak **mantıklı mı**? "Şekil" diye bir nesnenin alanı 0 mı? Bir alt class `area()`'yı override etmeyi **unutursa** ne olur — derleyici seni uyarır mı? Kağıda 2-3 cümle. (İpucu: Gün 5'te `abstract`.)
