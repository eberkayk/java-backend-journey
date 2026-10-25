package w3d4;

import java.util.List;

public class PairDemo {
    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("Dune", 1965);
        System.out.println(p);

        String title = p.getFirst();
        int year = p.getSecond();
        System.out.println(title);
        System.out.println(year);

        System.out.println(lastOf(List.of("a", "b", "c")));
        System.out.println(lastOf(List.of(3, 1, 4)));
    }

    public static <T> T lastOf(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty");
        }
        return list.getLast();
    }
}
