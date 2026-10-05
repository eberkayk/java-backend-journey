public class GCD {
    public static void main(String[] args) {
        System.out.println(gcd(48,18));
        System.out.println(gcd(18,48));
        System.out.println(gcd(7,13));
        System.out.println(gcd(5,0));
        System.out.println(gcd(100000,100000));
        System.out.println(lcm(48,18));
        System.out.println(lcm(18,48));
        System.out.println(lcm(7,13));
        System.out.println(lcm(5,0));
        System.out.println(lcm(100000,100000));
        System.out.println(gcd(0,0));
        System.out.println(lcm(0,0));
    }
    // negatif değerlerde -1 döndüren EBOB metodu
    public static int gcd(int a, int b) {
        if (a < 0 || b < 0) {
            return -1;
        }
        while (b > 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }
    //negatif sayılarda -1 döndüren EKOK metodu, int sınırlarını aşmayınız.
    public static int lcm(int a, int b) {
        if (a < 0 || b < 0) {
            return -1;
        }
        if (a == 0 || b == 0) {
            return 0;
        }
        return a / gcd(a,b) * b;
    }
}
