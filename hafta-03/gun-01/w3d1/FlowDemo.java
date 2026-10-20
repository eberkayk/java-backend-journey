package w3d1;

public class FlowDemo {
    static int parse(String s) {
        System.out.println("parse start: " + s);
        int n = Integer.parseInt(s);
        System.out.println("parse end");
        return n;
    }

    static void main(String[] args) {
        try {
            System.out.println("A");
            int x = parse("42");
            System.out.println("B " + x);
            int y = parse("4x2");
            System.out.println("C " + y);
        } catch (ArithmeticException e) {
            System.out.println("D " + e.getMessage());
        } finally {
            System.out.println("E");
        }
        System.out.println("F");
    }
}