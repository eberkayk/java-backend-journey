package w4d3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class OptionalPredict {
    public static void main(String[] args) {
        List<String> names = List.of("Deniz", "Ali", "Bora", "Ece");
        Optional<String> b = names.stream().filter(n -> n.startsWith("B")).findFirst();
        Optional<String> z = names.stream().filter(n -> n.startsWith("Z")).findFirst();

        System.out.println(b);
        System.out.println(z);
        System.out.println(b.isPresent() + " " + z.isEmpty());
        System.out.println(b.orElse("none") + " " + z.orElse("none"));
        System.out.println(b.map(String::length).orElse(0) + " " + z.map(String::length).orElse(0));
        b.ifPresent(n -> System.out.println("Found " + n));
        z.ifPresent(n -> System.out.println("Found " + n));
        System.out.println(names.stream().max(Comparator.comparingInt(String::length)).orElseThrow());
        System.out.println(z.orElseThrow());
    }
}
