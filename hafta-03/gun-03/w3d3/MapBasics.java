package w3d3;

import java.util.HashMap;
import java.util.Map;

public class MapBasics {
    public static void main(String[] args) {
        Map<String, Integer> stock = new HashMap<>();
        stock.put("apple", 5);
        stock.put("pear", 3);
        System.out.println(stock.get("apple"));            // 1
        System.out.println(stock.get("banana"));           // 2
        System.out.println(stock.put("apple", 10));        // 3
        System.out.println(stock.get("apple"));            // 4
        System.out.println(stock.size());                  // 5
        System.out.println(stock.containsKey("pear"));     // 6
        System.out.println(stock.containsValue(3));        // 7
        System.out.println(stock.remove("pear"));          // 8
        System.out.println(stock.remove("pear"));          // 9
        System.out.println(stock.getOrDefault("kiwi", 0)); // 10
        int kiwi = stock.get("kiwi");                      // 11: ne olur?
        System.out.println(kiwi);
    }
}
