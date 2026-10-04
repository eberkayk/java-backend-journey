import java.util.Scanner;
public class PrimeCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a positive number: ");
        int number = scanner.nextInt();
        if (number < 0) {
            System.out.println("Invalid entry.");
        } else if ( number == 0 || number == 1) {
            System.out.println("Your number is not prime.");
        } else {
            boolean isPrime = true;
            for (int i = 2; i <= number / i; i++) {
                if (number % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                System.out.println("Your number is prime.");
            } else {
                System.out.println("Your number is not prime.");
            }
        }

    }
}