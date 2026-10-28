package w4d2;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Algo2 {
    public static void main(String[] args) {

    }

    public static int countWords(String[] words1, String[] words2) {
        Map<String, Long> map1 = Arrays.stream(words1).collect(Collectors.groupingBy(e->e, Collectors.counting()));
        Map<String, Long> map2 = Arrays.stream(words2).collect(Collectors.groupingBy(e->e, Collectors.counting()));

        int count = 0;
        for (String s : map1.keySet()) {
            if (map1.get(s) == 1 && map2.getOrDefault(s, 0L) == 1) {
                count++;
            }

        }
        return count;
    }

}
