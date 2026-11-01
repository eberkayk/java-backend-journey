package w4d6;

import java.util.*;
import java.util.stream.Collectors;

public class Exam2 {
    public static void main(String[] args) {

    }

    public static List<String> topKFrequent(String[] words, int k) {
        Map<String, Long> map = Arrays.stream(words).collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        List<String> strings = new ArrayList<>();
        for (String s : map.keySet()) {
            strings.add(s);
        }
        strings.sort(Comparator.comparing((String w) -> map.get(w)).reversed().thenComparing(Comparator.naturalOrder()));
        return strings.stream().limit(k).toList();
    }
}
