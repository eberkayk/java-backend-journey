package w4d1;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ExpenseApp {
    public static void main(String[] args) {
        ExpenseTracker expenseTracker = new ExpenseTracker();
        expenseTracker.add(new Expense("Coffee", "Food", 45.5));
        expenseTracker.add(new Expense("Rent", "Home", 12000));
        expenseTracker.add(new Expense("Bus card", "Transport", 15));
        expenseTracker.add(new Expense("Groceries", "Food", 640.75));
        expenseTracker.add(new Expense("Gum", "Food", 12.5));
        expenseTracker.add(new Expense("Internet", "Home", 450));

        Predicate<Expense> isFood = expense -> Objects.equals(expense.getCategory(), "Food");
        System.out.println("All:");
        expenseTracker.getAll().forEach(System.out::println);
        System.out.println("Food: ");
        expenseTracker.filter(isFood).forEach(System.out::println);
        System.out.println("Over 100: ");
        expenseTracker.filter(compare -> compare.getAmount() > 100).forEach(System.out::println);
        System.out.println("By amount desc: ");
        expenseTracker.sorted((a, b) -> Double.compare(b.getAmount(),a.getAmount())).forEach(System.out::println);
        System.out.println("By category, then amount");
        expenseTracker.sorted(Comparator.comparing(Expense::getCategory).thenComparing(Expense::getAmount)).forEach(System.out::println);
        System.out.println("Food total: " + expenseTracker.total(isFood));
        System.out.println("Removed: " + expenseTracker.removeWhere(expense -> expense.getAmount() <20));
        System.out.println("Left: " + (expenseTracker.getAll().size()));
        try {
            expenseTracker.add(new Expense("Test", "Food", 0));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
