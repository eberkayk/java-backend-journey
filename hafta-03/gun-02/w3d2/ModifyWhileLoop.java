package w3d2;

import java.util.ArrayList;
import java.util.List;

public class ModifyWhileLoop {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Ali", "Ayse", "Can", "Aras"));
        names.removeIf(name -> name.startsWith("A"));
        System.out.println(names);
    }
}
