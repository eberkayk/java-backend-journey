package w4d2;

import java.util.Comparator;
import java.util.Objects;
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
        expenseTracker.add(new Expense("Taxi", "Transport", 230));

        Predicate<Expense> isFood = expense -> Objects.equals(expense.getCategory(), "Food");
        System.out.println("Food: ");
        expenseTracker.filter(isFood).forEach(System.out::println);
        System.out.println("Categories: " + expenseTracker.categories());
        System.out.println("Count by category: " + expenseTracker.countByCategory());
        System.out.println("Totals by category: " + expenseTracker.totalsByCategory());
        System.out.println("Top 3: ");
        expenseTracker.top(3).forEach(System.out::println);
        System.out.println("Food items: " + expenseTracker.descriptions("Food"));
        System.out.println("Any over 10000: " + expenseTracker.hasExpenseOver(10000));
        System.out.println("Any over 20000: " + expenseTracker.hasExpenseOver(20000));
        System.out.println("Total: " + expenseTracker.total(e -> e.getAmount() >= 0));
        try {
            expenseTracker.top(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
