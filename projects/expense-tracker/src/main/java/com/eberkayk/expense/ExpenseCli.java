package com.eberkayk.expense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

public class ExpenseCli {
    private static final Path FILE = Path.of("expenses.csv");
    private static final String USAGE = "Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM>";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE);
            return;
        }

        try {
            ExpenseTracker expenseTracker;
            if (Files.exists(FILE)) {
                expenseTracker = ExpenseTracker.loadFrom(FILE);
            } else {
                expenseTracker = new ExpenseTracker();
            }
            switch (args[0]) {
                case "add" -> {
                    if (args.length != 5) {
                        System.out.println(USAGE);
                        return;
                    }
                    LocalDate localDate = LocalDate.parse(args[1]);
                    Category category = Category.valueOf(args[3]);
                    double amount = Double.parseDouble(args[4]);
                    Expense expense = new Expense(localDate, args[2], category, amount);
                    expenseTracker.add(expense);
                    expenseTracker.saveTo(FILE);
                    System.out.println("Added: " + expense);
                }
                case "list" -> {
                    if (expenseTracker.getAll().isEmpty()) {
                        System.out.println("No expenses");
                    } else {
                        expenseTracker.sorted(Comparator.comparing(Expense::date)).forEach(System.out::println);
                    }
                }
                case "summary" -> {
                    System.out.println("Count: " + expenseTracker.getAll().size());
                    System.out.println("Total: " + expenseTracker.total(e -> true));
                    System.out.println("By category: " + expenseTracker.totalsByCategory());
                    System.out.println("By month: " + expenseTracker.totalsByMonth());
                }
                case "month" -> {
                    if (args.length != 2) {
                        System.out.println(USAGE);
                        return;
                    } else {
                        YearMonth ym = YearMonth.parse(args[1]);
                        LocalDate first = ym.atDay(1);
                        LocalDate last = ym.atEndOfMonth();
                        List<Expense> list = expenseTracker.between(first, last);
                        list.forEach(System.out::println);
                        System.out.println("Total: " + list.stream().mapToDouble(Expense::amount).sum());
                    }
                }
                default -> System.out.println(USAGE);
            }
        } catch (IllegalArgumentException | DateTimeException e) {
            System.out.println("Error: "+ e.getMessage());
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
