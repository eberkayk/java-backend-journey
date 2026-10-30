package w4d4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
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

    public void saveTo(Path path) throws IOException {
        Files.write(path,expenses.stream().map(Expense::toCsv).toList());
    }

    public static ExpenseTracker loadFrom(Path path) throws IOException {
        ExpenseTracker tracker = new ExpenseTracker();
        List<String> expenses1 = Files.readAllLines(path);
        for (String s : expenses1) {
            tracker.add(Expense.fromCsv(s));
        }
        return tracker;
    }

    public List<Expense> between(LocalDate from, LocalDate to) {
        return expenses.stream().filter(n -> !n.date().isAfter(to) && !n.date().isBefore(from)).sorted(Comparator.comparing(Expense::date)).toList();
    }

    public Map<YearMonth, Double> totalsByMonth() {
        return new TreeMap<>(expenses.stream().collect(Collectors.groupingBy(expense -> YearMonth.from(expense.date()), Collectors.summingDouble(Expense::amount))));
    }
}
