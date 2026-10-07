public class StringMethods {
    public static void main(String[] args) {
        String s = "  Java Backend Developer  ";
        System.out.println(s.length());
        System.out.println(s.strip().length());
        System.out.println(s.strip().charAt(0));
        System.out.println(s.strip().indexOf("Back"));
        System.out.println(s.strip().substring(5, 12));
        System.out.println(s.strip().split(" ").length);
        System.out.println(s.toLowerCase().contains("java"));
        System.out.println(s.strip().replace('a', '@'));

    }
}