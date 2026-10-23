package w3d3;

import java.util.HashMap;
import java.util.Map;

public class WordCount {
    public static void main(String[] args) {
        Map<String, Integer> counts = countWords("the cat and the hat and the bat");

        System.out.println(counts);

        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    public static Map<String, Integer> countWords(String text) {
        Map<String, Integer> result = new HashMap<>();
        for (String string : text.split(" ")) {
            result.put(string, result.getOrDefault(string, 0) + 1);
        }
        return result;
    }
}
