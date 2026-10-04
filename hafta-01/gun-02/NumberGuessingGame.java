import java.util.Scanner;
import java.util.Random;
public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;
        int tryCount = 0;
        int guess;
        do {
            System.out.print("Guess a number between 1 and 100: ");
            guess = scanner.nextInt();
            tryCount++;
            if (guess < 1 || guess > 100) {
                System.out.println("Invalid guess.");
            } else if (guess == randomNumber) {
                System.out.println("You've guessed it right. The number was: " + randomNumber);
            } else if (guess < randomNumber) {
                System.out.println("Higher!");
            } else {
                System.out.println("Lower!");
            }
        } while (guess != randomNumber);
        System.out.println("Try count: " + tryCount);

    }
}