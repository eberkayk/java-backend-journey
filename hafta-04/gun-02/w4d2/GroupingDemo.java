package w4d2;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GroupingDemo {
    public static void main(String[] args) {
        List<String> words = List.of("apple", "avocado", "banana", "blueberry", "cherry", "apricot", "beet");
        System.out.println(new TreeMap<>(words.stream().map(n -> n.charAt(0)).collect(Collectors.groupingBy(Character::charValue, Collectors.counting()))));
        System.out.println(new TreeMap<>(words.stream().collect(Collectors.groupingBy(String::length))));
        System.out.println(words.stream().filter(n -> n.startsWith("b")).map(Object::toString).collect(Collectors.joining(" | ")));
        System.out.println(words.stream().map(String::length).mapToInt(n->n).sum());
    }
}
