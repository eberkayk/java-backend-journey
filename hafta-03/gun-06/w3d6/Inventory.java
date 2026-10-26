package w3d6;

import java.util.*;

public class Inventory implements Iterable<Product>{
    private final Map<String, Product> products = new HashMap<>();

    public void add(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        if (products.get(p.getName()) != null) {
            throw new IllegalArgumentException("Product already exists: " + p.getName());
        }
        products.put(p.getName(), p);
    }

    public Product find(String name) {
        if (!products.containsKey(name)) {
            throw new IllegalArgumentException("No such product: " + name);
        }
        return products.get(name);
    }

    public void sell(String name, int qty) {
        find(name);

        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity must be positive");
        }

        if (qty > products.get(name).getStock()) {
            throw new InvalidQuantityException("Not enough stock: " + name);
        }

        products.get(name).decreaseStock(qty);
    }

    public List<Product> sortedByName() {
        List<Product> products1 = new ArrayList<>();
        for (Product product : products.values()){
            products1.add(product);
        }
        products1.sort(null);
        return List.copyOf(products1);
    }

    public List<Product> sortedByPriceDesc() {
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice).reversed().thenComparing(Product::getName);
        List<Product> products1 = new ArrayList<>();
        for (Product product : products.values()){
            products1.add(product);
        }
        products1.sort(comparator);
        return List.copyOf(products1);
    }

    public int removeOutOfStock() {
        Iterator<Product> it = products.values().iterator();
        int removed = 0;
        while (it.hasNext()) {
            int n = it.next().getStock();
            if (n <= 0) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    @Override
    public Iterator<Product> iterator() {
        return sortedByName().iterator();
    }
}
