import java.util.Scanner;
public class GradeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a grade: ");
        int grade = scanner.nextInt();
        if (grade > 100 || grade < 0){
            System.out.println("Invalid Score");
        }else if (grade >= 90){
            System.out.println("AA");
        } else if (grade >= 80) {
            System.out.println("BA");
        } else if (grade >= 70) {
            System.out.println("BB");
        } else if (grade >= 60) {
            System.out.println("CB");
        } else if (grade >= 50) {
            System.out.println("CC");
        } else {
            System.out.println("FF");
        }
    }
}