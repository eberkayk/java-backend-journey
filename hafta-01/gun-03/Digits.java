public class Digits {
    public static void main(String[] args) {
        System.out.println(digitCount(12345));
        System.out.println(digitCount(0));
        System.out.println(digitCount(-2));
        System.out.println(digitCount(7));
        System.out.println(digitCount(Integer.MAX_VALUE));
        System.out.println(digitSum(-3));
        System.out.println(digitSum(0));
        System.out.println(digitSum(345));
        System.out.println(digitSum(7));
        System.out.println(digitSum(Integer.MAX_VALUE));
    }
    //negatif sayılar için 0 döndürür
    public static int digitCount(int n) {
        int count = 0;
        if (n < 0) {
            return 0;
        }
        if (n == 0) {
            return 1;
        }
        while (n > 0) {
            n = n / 10;
            count++;
        }
        return count;
    }
    //negatif değerler için -1 döndürür.
    public static int digitSum(int n) {
        int sum = 0;
        if (n < 0) {
            return -1;
        }
        while (n > 0) {
            int digit = n % 10;
            n = n / 10;
            sum += digit;
        }
        return sum;
    }
}
