package w4d3;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExpenseApp {
    public static void main(String[] args) {
        ExpenseTracker expenseTracker = new ExpenseTracker();
        expenseTracker.add(new Expense("Coffee", Category.FOOD, 45.5));
        expenseTracker.add(new Expense("Rent", Category.HOME, 12000));
        expenseTracker.add(new Expense("Bus card", Category.TRANSPORT, 15));
        expenseTracker.add(new Expense("Groceries", Category.FOOD, 640.75));
        expenseTracker.add(new Expense("Gum", Category.FOOD, 12.5));
        expenseTracker.add(new Expense("Internet", Category.HOME, 450));
        expenseTracker.add(new Expense("Taxi", Category.TRANSPORT, 230));
        ExpenseTracker expenseTracker1 = new ExpenseTracker();

        Predicate<Expense> isFood = expense -> expense.category() == Category.FOOD;
        System.out.println("Food: ");
        expenseTracker.filter(isFood).forEach(System.out::println);
        System.out.println("Categories: " + expenseTracker.categories());
        System.out.println("Labels: " + Arrays.stream(Category.values()).map(Category::getLabel).collect(Collectors.joining(", ")));
        System.out.println("Count by category: " + expenseTracker.countByCategory());
        System.out.println("Totals by category: " + expenseTracker.totalsByCategory());
        System.out.println("Food items: " + expenseTracker.descriptions(Category.FOOD));
        System.out.println("Most expensive: " + expenseTracker.mostExpensive().map(Expense::description).orElse("none"));
        System.out.println("Empty tracker most expensive: " + expenseTracker1.mostExpensive().map(Expense::description).orElse("none"));
        System.out.println("Gum price: " + expenseTracker.findByDescription("gum").map(Expense::amount).orElse(0.0));
        System.out.println("Pizza price: " + expenseTracker.findByDescription("Pizza").map(Expense::amount).orElse(0.0));
        System.out.println("Same gum: " + expenseTracker.findByDescription("Gum").orElseThrow().equals(new Expense("Gum", Category.FOOD, 12.5)));
        System.out.println("Top 3: ");
        expenseTracker.top(3).forEach(System.out::println);
        System.out.println("Food items: " + expenseTracker.descriptions(Category.FOOD));
        System.out.println("Any over 10000: " + expenseTracker.hasExpenseOver(10000));
        System.out.println("Any over 20000: " + expenseTracker.hasExpenseOver(20000));
        System.out.println("Total: " + expenseTracker.total(e -> e.amount() >= 0));
        try {
            expenseTracker.top(0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            new Expense("Tea", null, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            Category.valueOf("Pizza");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
