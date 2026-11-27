package com.eberkayk.expense;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseTrackerTest {
    private ExpenseTracker expenseTracker;
    @BeforeEach
    void setUp() {
        expenseTracker = new ExpenseTracker();
        expenseTracker.add(new Expense(LocalDate.of(2026, 11, 1), "Market", Category.FOOD, 300));
        expenseTracker.add(new Expense(LocalDate.of(2026, 11, 15), "Kira", Category.HOME, 5000));
        expenseTracker.add(new Expense(LocalDate.of(2026, 12, 2), "Otobus", Category.TRANSPORT, 40));
    }

    @Test
    void checkTotals() {
        double totalFood = expenseTracker.totalsByCategory().get(Category.FOOD);
        assertEquals(300.0, totalFood, 0.001);
        assertEquals(5340.0, expenseTracker.total(e -> true), 0.001);
    }

    @Test
    void checkSortTop() {
        assertEquals("Kira", expenseTracker.top(2).get(0).description());
        assertEquals("Market", expenseTracker.top(2).get(1).description());
        assertThrows(IllegalArgumentException.class, () -> expenseTracker.top(0));
    }

    @Test
    void checkBetweenDates() {
        assertEquals(2, expenseTracker.between(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 15)).size());
        assertEquals("Market", expenseTracker.between(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 15)).get(0).description());
    }

    @Test
    void checkRemoveWhere() {
        assertEquals(1, expenseTracker.removeWhere(e -> e.amount() < 100));
        assertEquals(2, expenseTracker.getAll().size());
    }

    @Test
    void saveAndLoad(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("test.csv");
        expenseTracker.saveTo(file);
        ExpenseTracker newExpenseTracker = ExpenseTracker.loadFrom(file);
        assertEquals(expenseTracker.getAll(), newExpenseTracker.getAll());
    }

    @Test
    void totalByMonth() {
        assertEquals(5300, expenseTracker.totalsByMonth().get(YearMonth.of(2026,11)), 0.001);
    }

    @Test
    void mostExpensive() {
        assertEquals("Kira", expenseTracker.mostExpensive().get().description());
    }

    @Test
    void sortedWithComparator() {
        assertEquals("Otobus", expenseTracker.sorted(Comparator.comparingDouble(Expense::amount)).getFirst().description());
    }


}
