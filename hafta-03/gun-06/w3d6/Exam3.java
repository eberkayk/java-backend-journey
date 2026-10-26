package w3d6;

import java.util.*;

public class Exam3 {
    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>(List.of("b","a","b"));
        System.out.println(removeDuplicates(stringList));
        List<Integer> integerList = new ArrayList<>(List.of(3, 1, 3, 2, 1));
        System.out.println(removeDuplicates(integerList));
        List<String> stringList1 = new ArrayList<>();
        System.out.println(removeDuplicates(stringList1));
        System.out.println(stringList);
        System.out.println(integerList);
        System.out.println(stringList1);
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        List<T> list1 = new ArrayList<>(List.copyOf(list));
        List<T> result = new ArrayList<>();
        Set<T> seen = new HashSet<>();
        for (T t: list1) {
            if (seen.add(t)) {
                result.add(t);
            }
        }
        return result;
    }
}
