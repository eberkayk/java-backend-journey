package w4d4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileDemo {
    public static void main(String[] args) throws IOException {
        Path p = Path.of("notes.txt");
        List<String> strings = new ArrayList<>();
        strings.add("first");
        strings.add("second");
        strings.add("third");
        Files.write(p, strings);
        List<String> read = Files.readAllLines(p);
        System.out.println("Lines: " + read.size());
        int x = 1;
        for (String s : read) {
            System.out.println(x + ": " + s);
            x++;
        }
        List<String> fourth =  new ArrayList<>();
        fourth.add("fourth");
        Files.write(p, fourth, StandardOpenOption.APPEND);
        System.out.println("After append: " + Files.readAllLines(p));
        List<String> only = new ArrayList<>();
        only.add("only");
        Files.write(p, only);
        System.out.println("After overwrite: " + Files.readAllLines(p));
        try {
            Path d = Path.of("missing.txt");
            Files.readAllLines(d);
        } catch (IOException e) {
            System.out.println("Error: " + e.getClass().getSimpleName() + " " + e.getMessage());
        }

    }
}
