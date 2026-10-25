package w3d5;

import java.util.*;

public class IteratorDemo {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Ali", "Bora", "Can", "Deniz"));
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            String n = it.next();
            if (n.length() == 3) {
                it.remove();
            }
        }
        System.out.println(names);                      // (1)

        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4));
        nums.removeIf(x -> x % 2 == 0);
        System.out.println(nums);                       // (2)

        Iterator<Integer> it2 = nums.iterator();
        System.out.println(it2.next());                 // (3)
        System.out.println(it2.next());                 // (4)
        System.out.println(it2.hasNext());              // (5)

        try {
            it2.next();
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName());   // (6)
        }

        List<String> letters = new ArrayList<>(List.of("a", "b", "c"));
        try {
            Iterator<String> iterator = letters.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                if (s.equals("a")) {
                    iterator.remove();
                }
            }
            System.out.println(letters);
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName());   // (7)
        }
    }
}