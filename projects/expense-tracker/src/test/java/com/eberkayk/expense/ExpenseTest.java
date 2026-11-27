package com.eberkayk.expense;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseTest {
    @Test
    void createExpenseWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Expense(LocalDate.of(2026, 11, 27), "description", Category.FOOD, 0.0));
    }

    @Test
    void descriptionWithComma() {
        assertThrows(IllegalArgumentException.class, () -> new Expense(LocalDate.of(2026, 11, 27), "Su, ekmek", Category.FOOD, 1));
    }

    @Test
    void compareFromCSVToCSV() {
        Expense expense = new Expense(LocalDate.of(2026, 11, 27), "description", Category.FOOD, 1);
        assertEquals(expense, Expense.fromCsv(expense.toCsv()));
    }


}
