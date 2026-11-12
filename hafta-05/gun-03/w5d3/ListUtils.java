package w5d3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class ListUtils {
    private ListUtils() {}

    public static <T> List<T> topN(List<T> list, Comparator<T> order, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Bad n: " + n);
        }

        return list.stream().sorted(order).limit(n).toList();
    }

    public static <T> Optional<T> firstMatch(List<T> list, Predicate<T> condition) {
        return list.stream().filter(condition).findFirst();
    }

    public static <A, B> Pair<B, A> swap(Pair<A, B> pair) {
        return new Pair<>(pair.getSecond(), pair.getFirst());
    }
}
