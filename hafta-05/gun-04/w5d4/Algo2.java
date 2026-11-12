package w5d4;

import java.util.Map;
import java.util.TreeMap;

public class Algo2 {
    public static void main(String[] args) {
        System.out.println(sortSentence("is2 sentence4 This1 a3"));
    }

    public static String sortSentence(String s) {
        String[] words = s.split(" ");
        Map<Integer, String> map = new TreeMap<>();
        for (int i = 0; i < words.length; i++) {
            StringBuilder sb = new StringBuilder(words[i]);
            int order = Integer.parseInt(sb.substring(words[i].length() - 1));
            String word = sb.substring(0, words[i].length() - 1);
            map.put(order, word);
        }
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < words.length + 1; i++) {
            result.append(map.get(i));
            result.append(" ");
        }
        return result.toString().trim();
    }
}
