package w4d2;

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
        return expenses.stream().filter(c).mapToDouble(Expense::getAmount).sum();
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

    public List<String> categories() {
        return expenses.stream().map(Expense::getCategory).sorted().distinct().toList();
    }

    public Map<String, Long> countByCategory() {
        return new TreeMap<>(expenses.stream().map(Expense::getCategory).collect(Collectors.groupingBy(n->n, Collectors.counting())));
    }

    public Map<String, Double> totalsByCategory() {
        return new TreeMap<>(expenses.stream().collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount))));
    }

    public List<Expense> top(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }
        return expenses.stream().sorted(Comparator.comparingDouble(Expense::getAmount).reversed()).limit(n).toList();
    }

    public boolean hasExpenseOver(double limit) {
        return expenses.stream().map(Expense::getAmount).anyMatch(n -> n > limit);
    }

    public String descriptions(String category) {
        return expenses.stream().filter(n -> Objects.equals(n.getCategory(), category)).map(Expense::getDescription).collect(Collectors.joining(", "));
    }
}
