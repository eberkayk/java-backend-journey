package w4d3;

import java.util.HashSet;
import java.util.List;

public class PointDemo {
    public static void main(String[] args) {
        Point p = new Point(1, 2);
        Point q = new Point(1, 2);
        Point r = new Point(4, 6);

        System.out.println(p);
        System.out.println(p.equals(q) + " " + (p == q));
        System.out.println(p.manhattan(r));
        System.out.println(r.x());
        System.out.println(new HashSet<>(List.of(p, r)).contains(q));

        try {
            new Point(-1, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
