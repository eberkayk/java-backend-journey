package w4d1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LambdaConvert {
    static void main(String[] args) {
        Comparator<String> byLength = (a, b) -> Integer.compare(a.length(), b.length());

        Comparator<String> byLength2 = Comparator.comparingInt(String::length);

        Runnable hello = () -> System.out.println("Hello");

        Predicate<Integer> isEven = n -> n % 2 == 0;

        List<String> names = new ArrayList<>(List.of("Deniz", "Ali", "Bora", "Ce"));
        names.sort(byLength);
        System.out.println(names);
        names.sort(byLength.reversed());
        System.out.println(names);
        hello.run();
        System.out.println(isEven.test(4) + " " + isEven.test(7));

        names.sort(byLength2);
        System.out.println(names);
    }
}
