package w3d6;

public class Product implements Comparable<Product>{
    private final String name;
    private final double price;
    private int stock;


    public Product(String name, double price, int stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.name = name;
        if (price < 0) {
            throw new InvalidQuantityException("Price must not be negative");
        }
        this.price = price;
        if (stock < 0) {
            throw new InvalidQuantityException("Stock must not be negative");
        }
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void decreaseStock(int qty) {
        stock -= qty;
    }

    @Override
    public int compareTo(Product o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return getName() + " | " + getPrice() + " | " + getStock();
    }
}
