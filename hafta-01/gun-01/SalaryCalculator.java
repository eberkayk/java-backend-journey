import java.util.Scanner;
public class SalaryCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a value for gross salary: ");
        double grossSalary = scanner.nextDouble();
        System.out.println("Enter a value for tax rate: ");
        double taxRate = scanner.nextDouble();
        double tax = grossSalary * taxRate / 100;
        double netSalary = grossSalary * (100 - taxRate) / 100;
        System.out.printf("Net: %.2f%nTax: %.2f%n", netSalary, tax);
    }
}