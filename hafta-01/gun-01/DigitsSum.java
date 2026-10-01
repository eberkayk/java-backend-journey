import java.util.Scanner;
public class DigitsSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a three digit number to find its digits values sum: ");
        int number = scanner.nextInt();
        int firstDigit = number / 100;
        int secondDigit = (number % 100) / 10;
        int thirdDigit = (number % 10);
        int totalValue = firstDigit + secondDigit + thirdDigit;
        System.out.println("Digit values sum: " + totalValue);
    }
}