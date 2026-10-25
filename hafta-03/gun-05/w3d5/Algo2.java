package w3d5;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Algo2 {
    public static void main(String[] args) {

    }

    public static boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int i : arr) {
            count.put(i, count.getOrDefault(i, 0) + 1);
        }
        Set<Integer> set = new HashSet<>();
        for (int i : count.values()) {
            if (set.add(i) == false) {
                return false;
            }
        }
        return true;
    }
}
//
//Map<Character, Integer> count = new HashMap<>();
//for (char ch : "banana".toCharArray()) {
//        count.put(ch, count.getOrDefault(ch, 0) + 1);
//        }
//        System.out.println(count);   // {a=3, b=1, n=2}  (sıra farklı olabilir)