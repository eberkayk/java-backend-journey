import java.util.Scanner;

public class Grades {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student count: ");
        int studentCount = scanner.nextInt();
        int[] grades = new int[studentCount];
        int index = 0;
        while (index < grades.length) {
            System.out.print("Enter a score for student number " + (index+1) + ": ");
            int score = scanner.nextInt();
            if (score <= 100 && score >= 0) {
                grades[index] = score;
                index++;

            }
        }

        System.out.println("Average score: " + ArrayStats.average(grades));
        System.out.println("Max score: " + ArrayStats.max(grades));
        System.out.println("Min score: " + ArrayStats.min(grades));

        for (int i = 0; i < grades.length; i++) {
            System.out.println("Student " + (i+1) + ": " + grades[i] + " -> " + GradeWithReturn.gradeFor(grades[i]));
        }
        System.out.println("Student count above average: " + ArrayStats.countGreaterThan(grades, (int)ArrayStats.average(grades)));
    }
}
