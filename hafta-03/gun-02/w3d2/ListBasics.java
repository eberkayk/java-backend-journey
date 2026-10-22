package w3d2;

import java.util.ArrayList;
import java.util.List;

public class ListBasics {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Ali");
        names.add("Ayse");
        names.add("Can");
        names.add(1, "Zeynep");
        System.out.println(names);                      // 1
        System.out.println(names.size());               // 2
        System.out.println(names.get(2));               // 3
        names.set(0, "Veli");
        System.out.println(names);                      // 4
        System.out.println(names.remove("Ayse"));       // 5
        System.out.println(names.remove("Mert"));       // 6
        System.out.println(names.indexOf("Can"));       // 7
        System.out.println(names.contains("Zeynep"));   // 8
        System.out.println(names.get(3));               // 9
    }
}
