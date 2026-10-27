package w4d1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionDemo {
    public static void main(String[] args) {
        Predicate<String> isLong = s -> s.length() > 3;
        Function<String, Integer> len = String::length;
        Consumer<String> shout = s -> System.out.println(s.toUpperCase() + "!");
        Supplier<List<String>> maker = () -> new ArrayList<>();

        System.out.println(isLong.test("Ali") + " " + isLong.test("Deniz"));
        System.out.println(len.apply("Java") * 2);
        shout.accept("hey");
        List.of("a", "bc").forEach(shout);
        List<String> a = maker.get();
        List<String> b = maker.get();
        a.add("x");
        System.out.println(a.size() + " " + b.size() + " " + (a == b));
    }
}
