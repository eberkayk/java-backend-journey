package w4d3;

public record Expense(String description, Category category, double amount) {
    public Expense{
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be blank");
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
        return description + " (" + category.getLabel() + ") " + amount;
    }
}
