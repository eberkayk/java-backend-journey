package w4d3;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExpenseTracker {
    private final List<Expense> expenses = new ArrayList<>();

    public void add(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        expenses.add(expense);
    }

    public List<Expense> getAll() {
        return List.copyOf(expenses);
    }

    public List<Expense> filter(Predicate<Expense> c) {
        return expenses.stream().filter(c).toList();
    }

    public List<Expense> sorted(Comparator<Expense> o) {
        return expenses.stream().sorted(o).toList();
    }

    public double total(Predicate<Expense> c) {
        return expenses.stream().filter(c).mapToDouble(Expense::amount).sum();
    }

    public int removeWhere(Predicate<Expense> c) {

        int count = 0;
        Iterator<Expense> it = expenses.iterator();
        while (it.hasNext()) {
            Expense expense = it.next();
            if (c.test(expense)) {
                it.remove();
                count++;
            }
        }
        return count;
    }

    public List<Category> categories() {
        return expenses.stream().map(Expense::category).sorted().distinct().toList();
    }

    public Map<Category, Long> countByCategory() {
        return new TreeMap<>(expenses.stream().map(Expense::category).collect(Collectors.groupingBy(n->n, Collectors.counting())));
    }

    public Map<Category, Double> totalsByCategory() {
        return new TreeMap<>(expenses.stream().collect(Collectors.groupingBy(Expense::category, Collectors.summingDouble(Expense::amount))));
    }

    public List<Expense> top(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }
        return expenses.stream().sorted(Comparator.comparingDouble(Expense::amount).reversed()).limit(n).toList();
    }

    public boolean hasExpenseOver(double limit) {
        return expenses.stream().map(Expense::amount).anyMatch(n -> n > limit);
    }

    public String descriptions(Category category) {
        return expenses.stream().filter(n -> n.category() == category).map(Expense::description).collect(Collectors.joining(", "));
    }

    public Optional<Expense> mostExpensive() {
        return expenses.stream().max(Comparator.comparingDouble(Expense::amount));
    }

    public Optional<Expense> findByDescription(String description) {
        return expenses.stream().filter(n -> n.description().equalsIgnoreCase(description)).findFirst();
    }
}
