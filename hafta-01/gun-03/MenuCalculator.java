import java.util.Scanner;

public class MenuCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            printMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter first number: ");
                    double number1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    double number2 = scanner.nextDouble();
                    System.out.println("Result: " + add(number1, number2));
                }
                case 2 -> {
                    System.out.print("Enter first number: ");
                    double number1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    double number2 = scanner.nextDouble();
                    System.out.println("Result: " + subtract(number1, number2));
                }
                case 3 -> {
                    System.out.print("Enter first number: ");
                    double number1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    double number2 = scanner.nextDouble();
                    System.out.println("Result: " + multiply(number1, number2));
                }
                case 4 -> {
                    System.out.print("Enter first number: ");
                    double number1 = scanner.nextDouble();
                    System.out.print("Enter second number: ");
                    double number2 = scanner.nextDouble();
                    if (number2 == 0) {
                        System.out.println("Divide by zero error!");
                    } else {
                        System.out.println("Result: " + divide(number1, number2));
                    }
                }
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Choose again.");
            }
        } while (choice != 0);
    }
    public static void printMenu() {
        System.out.println("Select an option:");
        System.out.println("1. Add");
        System.out.println("2. Subtract");
        System.out.println("3. Multiply");
        System.out.println("4. Divide");
        System.out.println("0. Exit");
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        return a / b;
    }

}