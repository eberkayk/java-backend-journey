import java.util.Scanner;
public class FahrenheitCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Fahrenheit'a çevirmek için bir Celsius değeri giriniz: ");
        double celsius = scanner.nextDouble();
        double fahrenheit = celsius * 9 / 5 + 32;
        System.out.println(celsius + " celsius'un Fahrenheit karşılığı " + fahrenheit + " Fahrenheit'dır.");
    }
}