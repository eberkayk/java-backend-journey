import java.util.Scanner;
public class ReverseNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter a positive number: ");
        int number = scanner.nextInt();
        if (number < 0) {
            System.out.println("Invalid entry.");
        } else {
            int reversed = 0;
            int remaining = number;
            while (remaining > 0) {
                int digit = remaining % 10;
                reversed = reversed * 10 + digit;
                remaining = remaining / 10;
            }
            System.out.println(reversed);
            if (reversed == number) {
                System.out.println("Palindrome");
            }
        }
    }
}