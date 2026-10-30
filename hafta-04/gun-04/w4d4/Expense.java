package w4d4;

import java.time.LocalDate;

public record Expense(LocalDate date, String description, Category category, double amount) {
    public Expense{
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be blank");
        }
        if (description.contains(",")) {
            throw new IllegalArgumentException("Description must not contain commas");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @Override
    public String toString() {
        return date + " " + description + " (" + category.getLabel() + ") " + amount;
    }

    public String toCsv() {
        return date + "," + description + "," + category.name() + "," + amount;
    }

    public static Expense fromCsv(String line) {
        String[] splitted = line.split(",");
        if (splitted.length != 4) {
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        LocalDate date1 = LocalDate.parse(splitted[0]);
        String description1 = splitted[1];
        Category category1 = Category.valueOf(splitted[2]);
        double amount1 = Double.parseDouble(splitted[3]);
        return new Expense(date1, description1, category1, amount1);
    }
}
