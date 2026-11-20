package com.eberkayk.expense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

public class ExpenseCli {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/expense_tracker";
    private static final String DB_USER = "postgres";
    private static final Path FILE = Path.of("expenses.csv");
    private static final String USAGE = "Usage: add <yyyy-MM-dd> <description> <CATEGORY> <amount> | list | summary | month <yyyy-MM> | import";

    public static void main(String[] args) {
        String DB_PASSWORD = System.getenv("DB_PASSWORD");
        if (DB_PASSWORD == null) {
            System.out.println("DB_PASSWORD is not set");
            return;
        }
        if (args.length == 0) {
            System.out.println(USAGE);
            return;
        }

        try {
            ExpenseTracker expenseTracker = new ExpenseTracker();
            ExpenseRepository expenseRepository = new ExpenseRepository(DB_URL, DB_USER, DB_PASSWORD);
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
                    expenseRepository.save(expense);
                    System.out.println("Added: " + expense);
                }
                case "list" -> {
                    List<Expense> all = expenseRepository.findAll();
                    if (all.isEmpty()) {
                        System.out.println("No expenses");
                    } else {
                        all.forEach(System.out::println);
                    }
                }
                case "summary" -> {
                    List<Expense> expenseList = expenseRepository.findAll();
                    for (Expense e : expenseList) {
                        expenseTracker.add(e);
                    }
                    System.out.println("Count: " + expenseTracker.getAll().size());
                    System.out.println("Total: " + expenseTracker.total(e -> true));
                    System.out.println("By category: " + expenseRepository.totalsByCategory());
                    System.out.println("By month: " + expenseTracker.totalsByMonth());
                }
                case "month" -> {
                    if (args.length != 2) {
                        System.out.println(USAGE);
                        return;
                    } else {
                        YearMonth ym = YearMonth.parse(args[1]);
                        List<Expense> list = expenseRepository.findByMonth(ym);
                        list.forEach(System.out::println);
                        System.out.println("Total: " + list.stream().mapToDouble(Expense::amount).sum());
                    }
                }
                case "import" -> {
                    int count = expenseRepository.saveAll(ExpenseTracker.loadFrom(FILE).getAll());
                    System.out.println("Imported: " + count);
                }
                default -> System.out.println(USAGE);
            }
        } catch (IllegalArgumentException | DateTimeException e) {
            System.out.println("Error: "+ e.getMessage());
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
