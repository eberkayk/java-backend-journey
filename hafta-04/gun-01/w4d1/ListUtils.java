package w4d1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListUtils {
    public static void main(String[] args) {
        System.out.println(filter(List.of(3, 8, 1, 10, 6), n -> n > 5));                 // [8, 10, 6]
        System.out.println(map(List.of("Ali", "Deniz"), String::length));                 // [3, 5]
        System.out.println(map(filter(List.of(3, 8, 1, 10, 6), n -> n > 5), n -> n * 10)); // [80, 100, 60]
    }

    // list`'teki elemanlardan `condition.test(...)` `true` olanları, aynı sırayla, yeni listeye koyar.
    public static <T> List<T> filter(List<T> list, Predicate<T> condition) {
        List<T> copyList = new ArrayList<>();
        for (T t : list) {
            if (condition.test(t)) {
                copyList.add(t);
            }
        }
        return copyList;
    }

    //- `list`'teki her elemana `mapper.apply(...)` uygular, sonuçları aynı sırayla yeni listeye koyar.
    //- `<T, R>`: iki tip parametresi. Girdi elemanı T, çıktı elemanı R (Gün 16'daki `<T>`'nin iki tanelisi).
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        List<R> mappedList = new ArrayList<>();
        for (T t : list) {
            mappedList.add(mapper.apply(t));
        }
        return mappedList;
    }
}
