import java.util.Scanner;

public class Immutability {
    public static void main(String[] args) {
        String name = "Berkay";
        System.out.println(name);
        name.toUpperCase();
        System.out.println(name);
        name = name.toUpperCase();
        System.out.println(name);

        String a = "java";
        String b = "java";
        String c = new String("java");

        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a.equals(c));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write java");
        String input = scanner.next();
        System.out.println(input == "java");
        System.out.println(input.equals("java"));

        String string = "hello";
        shout(string);
        System.out.println(string);
    }

    public static void shout(String s) {
        s = s.toUpperCase() + "!";
    }
}
