package w2d1;

public class RectangleDemo {
    public static void main(String[] args) {
        Rectangle r1 = new Rectangle(3,5);
        Rectangle r2 = new Rectangle(8,8);
        Rectangle r3 = new Rectangle(6);

        System.out.println(r1.area());
        System.out.println(r1.perimeter());
        System.out.println(r1.isSquare());

        System.out.println(r2.area());
        System.out.println(r2.perimeter());
        System.out.println(r2.isSquare());

        System.out.println(r3.area());
        System.out.println(r3.perimeter());
        System.out.println(r3.isSquare());

        r1.scale(2);
        System.out.println(r1.area());
        System.out.println(r1.perimeter());
        System.out.println(r1.isSquare());



    }
}
