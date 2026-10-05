public class Overloading {
    public static void main(String[] args) {
        System.out.println(findMax(4,3,3));
        System.out.println(findMax(3,4.5));
        System.out.println(findMax(3,4));
    }

    public static int findMax(int x, int y) {
        if (x >= y) {
            return x;
        }
        return y;
    }

    public static int findMax(int x, int y, int z) {
        return findMax(findMax(x,y),z);
    }

    public static double findMax(double x, double y) {
        if (x >= y) {
            return x;
        }
        return y;
    }
}
