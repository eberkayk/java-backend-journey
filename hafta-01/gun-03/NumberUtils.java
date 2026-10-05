public class NumberUtils {
    public static void main(String[] args) {
        System.out.println(isPrime(-1));
        System.out.println(isPrime(0));
        System.out.println(isPrime(1));
        System.out.println(isPrime(2));
        System.out.println(isPrime(4));
        System.out.println(isPrime(97));
        System.out.println(isPrime(100));
        System.out.println(isPrime(1000000));
        System.out.println(reverse(-1));
        System.out.println(reverse(0));
        System.out.println(reverse(1));
        System.out.println(reverse(123));
        System.out.println(reverse(12321));
        System.out.println(reverse(1000000009));
        System.out.println(isPalindrome(-1));
        System.out.println(isPalindrome(0));
        System.out.println(isPalindrome(1));
        System.out.println(isPalindrome(2));
        System.out.println(isPalindrome(12));
        System.out.println(isPalindrome(123454321));
        System.out.println(isPalindrome(12321));
        System.out.println(factorial(-3));
        System.out.println(factorial(0));
        System.out.println(factorial(1));
        System.out.println(factorial(2));
        System.out.println(factorial(5));
        System.out.println(factorial(20));
        System.out.println(sumTo(-1));
        System.out.println(sumTo(0));
        System.out.println(sumTo(1));
        System.out.println(sumTo(2));
        System.out.println(sumTo(10));
        System.out.println(sumTo(2034567890));
        System.out.println(reverse(120));

    }
    // negatif değerler için false döndürür.
    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i <= number / i; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }
    // int sınırlarından taşmaması için dönüş tipi ve reversed değişkeni long olarak güncellendi. negatif sayılar için 0 döndürülüyor.
    public static long reverse(int number) {
        long reversed = 0;
        if (number < 0) {
            return 0;
        }
        int remaining = number;
        while (remaining > 0) {
            int digit = remaining % 10;
            reversed = reversed * 10 + digit;
            remaining = remaining / 10;
        }
        return reversed;
    }
    // negatif değerler için false döndürür
    public static boolean isPalindrome(int number) {
        long reverseNumber = reverse(number);
        return reverseNumber == number;

    }

    //  factorial negatif değerler için 0 döndürüyor. 20'den sonraki değerler long'un üst sınırından taşacağı için kullanılmamalı.
    public static long factorial(int number) {
        if (number < 0) {
            return 0;
        }
        long factorial = 1;
        for (int i = 2; i <= number; i++){
            factorial *= i;
        }
        return factorial;
    }
    // negatif değerler için 0 döndürür
    public static long sumTo(int number) {
        long sum = 0;
        for (int i = 1; i <= number; i++) {
            sum += i;
        }
        return sum;
    }
}
