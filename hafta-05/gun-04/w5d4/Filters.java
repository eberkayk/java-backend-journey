package w5d4;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Filters {
    private Filters() {}

    public static Predicate<Order> minTotal(int min) {
        return o -> o.total() >= min;
    }

    public static Predicate<Order> byCustomer(String customer) {
        return order -> Objects.equals(order.customer(), customer);
    }

    public static Function<Order, String> label() {
        return o -> o.id() + ":" + o.customer() + ":" + o.total();
    }

}
