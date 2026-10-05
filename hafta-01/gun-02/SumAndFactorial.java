import java.util.Scanner;
public class SumAndFactorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        if (number < 0) {
            System.out.println("Invalid number");
            return;
        }
        int sum = 0;
        for (int i = 1; i <= number; i++) {
            sum += i;
        }
        long factorial = 1;
        for (int i = 2; i <= number; i++){
            factorial *= i;
        }
        System.out.println("Sum: " + sum);
        System.out.println("Factorial: " + factorial);

    }

}