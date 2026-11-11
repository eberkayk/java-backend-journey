package w5d2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Algo2 {
    public static void main(String[] args) {

    }

    public static boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (pattern.length() != words.length) {
            return false;
        }
        Map<String, String> wordMap = new HashMap<>();
        Map<String, String> charMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (wordMap.containsKey(words[i]) && !Objects.equals(wordMap.get(words[i]), String.valueOf(pattern.charAt(i)))) {
                return false;
            }
            if (charMap.containsKey(String.valueOf(pattern.charAt(i))) && !Objects.equals(charMap.get(String.valueOf(pattern.charAt(i))), words[i])) {
                return false;
            }
            wordMap.put(words[i], String.valueOf(pattern.charAt(i)));
            charMap.put(String.valueOf(pattern.charAt(i)), words[i]);
        }
        return true;
    }
}
