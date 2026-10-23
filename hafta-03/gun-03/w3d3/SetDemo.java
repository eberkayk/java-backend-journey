package w3d3;

import java.util.HashSet;
import java.util.Set;

public class SetDemo {
    public static void main(String[] args) {
        Set<String> tags = new HashSet<>();
        System.out.println(tags.add("java"));       // 1
        System.out.println(tags.add("spring"));     // 2
        System.out.println(tags.add("java"));       // 3
        System.out.println(tags.size());            // 4
        tags.remove("java");
        System.out.println(tags);                   // 5

        Set<Book> books = new HashSet<>();
        books.add(new Book("111", "Dune", "Frank Herbert"));
        System.out.println(books.add(new Book("111", "Emma", "Jane Austen")));  // 6
        System.out.println(books.size());                                       // 7
    }
}
