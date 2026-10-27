package w4d1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ExpenseTracker {
    private final List<Expense> expenses = new ArrayList<>();

    public void add(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        expenses.add(expense);
    }

    public List<Expense> getAll() {
        List<Expense> allExpenses = new ArrayList<>();
        for (Expense e : expenses) {
            allExpenses.add(e);
        }

        return List.copyOf(allExpenses);
    }

    public List<Expense> filter(Predicate<Expense> c) {
        List<Expense> filteredExpenses = new ArrayList<>();
        for (Expense e : expenses) {
            if (c.test(e)) {
                filteredExpenses.add(e);
            }
        }
        return filteredExpenses;
    }

    public List<Expense> sorted(Comparator<Expense> o) {
        List<Expense> allExpenses = new ArrayList<>();
        for (Expense e : expenses) {
            allExpenses.add(e);
        }
        allExpenses.sort(o);
        return allExpenses;
    }

    public double total(Predicate<Expense> c) {
        List<Expense> all = getAll();
        double total = 0;
        for (Expense e : all) {
            if (c.test(e)) {
                total += e.getAmount();
            }
        }
        return total;
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
}
