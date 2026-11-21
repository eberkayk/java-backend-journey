package com.eberkayk.expense;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExpenseRepository {
    private final String url;
    private final String user;
    private final String password;

    public ExpenseRepository(String url, String user, String password) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Url is blank");
        }
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User is blank");
        }
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void save(Expense expense) throws SQLException {
        if (expense == null) {
            throw new IllegalArgumentException("Expense is null");
        }
        String sql = "INSERT INTO expenses (spent_on, description, category, amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, expense.date());
            ps.setString(2, expense.description());
            ps.setString(3, expense.category().name());
            ps.setDouble(4, expense.amount());
            ps.executeUpdate();
        }
    }

    public List<Expense> findAll() throws SQLException {
        String sql = "SELECT * FROM expenses ORDER BY spent_on ASC, id ASC";

        List<Expense> result = new ArrayList<>();
        try (Connection conn = connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    LocalDate date = rs.getObject("spent_on", LocalDate.class);
                    String description = rs.getString("description");
                    Category category = Category.valueOf(rs.getString("category"));
                    double amount = rs.getDouble("amount");
                    result.add(new Expense(date, description, category, amount));
                }
            }
        }
        return result;
    }

    public List<Expense> findByMonth(YearMonth month) throws SQLException {
        if (month == null) {
            throw new IllegalArgumentException("Month is null");
        }
        String sql = "SELECT * FROM expenses WHERE spent_on >= ? AND spent_on < ? ORDER BY spent_on ASC, id ASC";

        List<Expense> result = new ArrayList<>();
        try (Connection conn = connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, month.atDay(1));
            ps.setObject(2, month.plusMonths(1).atDay(1));
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    LocalDate date = rs.getObject("spent_on", LocalDate.class);
                    String description = rs.getString("description");
                    Category category = Category.valueOf(rs.getString("category"));
                    double amount = rs.getDouble("amount");
                    result.add(new Expense(date, description, category, amount));
                }
            }
        }
        return result;
    }

    public Map<Category, Double> totalsByCategory() throws SQLException {
        String sql = "SELECT category, SUM(amount) AS total_amount FROM expenses GROUP BY category";
        Map<Category, Double> result = new TreeMap<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category category = Category.valueOf(rs.getString("category"));
                    double amount = rs.getDouble("total_amount");
                    result.put(category, amount);
                }
            }
        }
        return result;
    }

    public int saveAll(List<Expense> expenses) throws SQLException {
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses is null");
        }
        int count = 0;
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO expenses (spent_on, description, category, amount) VALUES (?, ?, ?, ?)")) {

                for (Expense e : expenses) {
                    ps.setObject(1, e.date());
                    ps.setString(2, e.description());
                    ps.setString(3, e.category().name());
                    ps.setDouble(4, e.amount());
                    count += ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
        return count;
    }

    public List<Expense> findByCategory(Category category) throws SQLException {
        if (category == null) {
            throw new IllegalArgumentException("Category is null");
        }
        List<Expense> result = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE category = ? ORDER BY spent_on, id";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.name());
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    LocalDate date = rs.getObject("spent_on", LocalDate.class);
                    String description = rs.getString("description");
                    Category category1 = Category.valueOf(rs.getString("category"));
                    double amount = rs.getDouble("amount");
                    result.add(new Expense(date, description, category1, amount));
                }
            }
        }
        return result;
    }

    public double totalBetween(LocalDate from, LocalDate to) throws SQLException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date is null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Invalid range");
        }
        double total = 0.0;
        String sql = "SELECT amount FROM expenses WHERE spent_on >= ? AND spent_on <= ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, from);
            ps.setObject(2, to);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    double amount = rs.getDouble("amount");
                    total += amount;
                }
            }
        }
        return total;

    }

    public int deleteByDescription(String description) throws SQLException {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is blank");
        }
        String sql = "DELETE FROM expenses WHERE description = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, description);
            return ps.executeUpdate();
        }
    }

    public int replaceMonth(YearMonth month, List<Expense> expenses) throws SQLException {
        if (month == null) {
            throw new IllegalArgumentException("Month is null");
        }
        if (expenses == null) {
            throw new IllegalArgumentException("Expenses is null");
        }
        int result = 0;
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM expenses WHERE spent_on >= ? AND spent_on < ?");
                 PreparedStatement ps2 = conn.prepareStatement("INSERT INTO expenses (spent_on, description, category, amount) VALUES (?, ?, ?, ?)")) {
                ps.setObject(1, month.atDay(1));
                ps.setObject(2, month.plusMonths(1).atDay(1));
                ps.executeUpdate();

                for (Expense e : expenses) {
                    ps2.setObject(1, e.date());
                    ps2.setString(2, e.description());
                    ps2.setString(3, e.category().name());
                    ps2.setDouble(4, e.amount());
                    result += ps2.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
        return result;
    }
}
