package w4d4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class ExpenseApp {
    public static void main(String[] args) throws IOException {
        ExpenseTracker expenseTracker = new ExpenseTracker();
        expenseTracker.add(new Expense(LocalDate.of(2026,10,12), "Coffee", Category.FOOD, 45.5));
        expenseTracker.add(new Expense(LocalDate.of(2026, 10, 1), "Rent", Category.HOME, 12000));
        expenseTracker.add(new Expense(LocalDate.of(2026, 9, 28), "Bus card", Category.TRANSPORT, 15));
        expenseTracker.add(new Expense(LocalDate.of(2026, 10, 5), "Groceries", Category.FOOD, 640.75));
        expenseTracker.add(new Expense(LocalDate.of(2026, 10, 15), "Gum", Category.FOOD, 12.5));
        expenseTracker.add(new Expense(LocalDate.of(2026, 10, 3), "Internet", Category.HOME, 450));
        expenseTracker.add(new Expense(LocalDate.of(2026, 10, 20), "Taxi", Category.TRANSPORT, 230));

        Path file = Path.of("expenses.csv");
        expenseTracker.saveTo(file);
        System.out.println("Saved " + expenseTracker.getAll().size() + " expenses");
        System.out.println("File:");
        Files.readAllLines(file).forEach(System.out::println);
        ExpenseTracker tracker = ExpenseTracker.loadFrom(file);
        System.out.println("Loaded: " + tracker.getAll().size());
        System.out.println("Same data: " + expenseTracker.getAll().equals(tracker.getAll()));
        System.out.println("Oct 1-15:");
        tracker.between(LocalDate.of(2026,10,1),LocalDate.of(2026,10,15)).forEach(System.out::println);
        System.out.println("Totals by month: " + expenseTracker.totalsByMonth());
        LocalDate first = expenseTracker.getAll().stream().map(Expense::date).min(Comparator.naturalOrder()).orElseThrow();
        LocalDate second = expenseTracker.getAll().stream().map(Expense::date).max(Comparator.naturalOrder()).orElseThrow();
        System.out.println("Span: " + first + " -> " + second + " (" + ChronoUnit.DAYS.between(first, second) + " days)");
        try {
            new Expense(LocalDate.of(2026,10,1), "Tea, milk", Category.FOOD, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            Expense.fromCsv("2026-10-01,Tea");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            ExpenseTracker.loadFrom(Path.of("missing.csv"));
        } catch (IOException e) {
            System.out.println("Load error: " + e.getClass().getSimpleName() + " " + e.getMessage());
        }
    }
}
