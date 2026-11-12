package w5d4;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OrderReport {
    private final List<Order> orders;

    public OrderReport(List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("Orders must not be null");
        }
        this.orders = List.copyOf(orders);
    }

    public List<Order> matching(Predicate<Order> condition) {
        return orders.stream().filter(condition).toList();
    }

    public int totalRevenue() {
        if (orders.isEmpty()) {
            return 0;
        }

        return orders.stream().mapToInt(Order::total).sum();
    }

    public int totalRevenueLoop() {
        if (orders.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (Order o : orders) {
            sum += o.total();
        }
        return sum;
    }

    public List<String> labelsByTotalDesc() {
        return orders.stream().sorted(Comparator.comparingInt(Order::total).reversed().thenComparing(Order::id)).map(o -> Filters.label().apply(o)).toList();
    }

    public Optional<Order> biggestOrder() {
        return orders.stream().max(Comparator.comparingInt(Order::total));
    }

    public Map<String, Long> orderCountByCategory() {
        return new TreeMap<>(orders.stream().map(Order::category).collect(Collectors.groupingBy(o -> o, Collectors.counting())));
    }

    public Map<String, Integer> revenueByCustomer() {
        return new TreeMap<>(orders.stream().collect(Collectors.groupingBy(Order::customer, Collectors.summingInt(Order::total))));
    }

    public List<String> customersWhoBought(String category) {
        return orders.stream().filter(o -> Objects.equals(o.category(), category)).map(Order::customer).distinct().sorted().toList();
    }
}
