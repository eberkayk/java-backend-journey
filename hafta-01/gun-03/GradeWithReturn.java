import java.util.Scanner;
public class GradeWithReturn {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a score: ");
        int score = scanner.nextInt();
        System.out.println(gradeFor(score));
    }
    // negatif ve 100'den büyük skorlar için invalid score döndürür.
    public static String gradeFor(int score) {
        if (score < 0 || score > 100) {
            return "Invalid score";
        }
        if (score >= 90) {
            return "AA";
        }
        if (score >= 80) {
            return "BA";
        }
        if (score >= 70) {
            return "BB";
        }
        if (score >= 60) {
            return "CB";
        }
        if (score >= 50) {
            return "CC";
        }
        return "FF";
    }
}
