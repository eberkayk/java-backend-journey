package w5d4;

public record Order(String id, String customer, String category, int qty, int unitPrice) {
    public Order {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be blank");
        }

        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("Customer must not be blank");
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category must not be blank");
        }

        if (qty < 1) {
            throw new IllegalArgumentException("Bad qty: " + qty);
        }

        if (unitPrice < 0) {
            throw new IllegalArgumentException("Bad price: " + unitPrice);
        }
    }

    public int total() {
        return qty * unitPrice;
    }
}
