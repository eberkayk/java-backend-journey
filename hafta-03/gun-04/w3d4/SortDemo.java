package w3d4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortDemo {
    public static void main(String[] args) {
        System.out.println("apple".compareTo("banana") < 0);        // 1
        System.out.println("cat".compareTo("cat"));                 // 2
        System.out.println(Integer.compare(3, 9) > 0);              // 3

        List<String> names = new ArrayList<>(List.of("can", "Zeynep", "ali", "Bora"));
        Collections.sort(names);
        System.out.println(names);                                  // 4

        names.sort(Comparator.comparing(String::length));
        System.out.println(names);                                  // 5  (ipucu: eşit uzunluktakiler sırasını korur)

        names.sort(Comparator.comparing(String::length).reversed());
        System.out.println(names);                                  // 6

        List<Integer> fixed = List.of(3, 1, 2);
        Collections.sort(fixed);                                    // 7: ne olur?
    }
}
