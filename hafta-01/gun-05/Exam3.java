public class Exam3 {
    public static void main(String[] args) {
        System.out.println(compress("aaabccdddd"));
        System.out.println(compress("a"));
        System.out.println(compress(""));
        System.out.println(compress("abc"));

    }

    public static String compress(String s) {
        if (s.isBlank()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        char current = s.charAt(0);
        int count = 1;

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == current) {
                count++;
            } else {
                sb.append(current).append(count);
                current = s.charAt(i);
                count = 1;
            }
        }
        sb.append(current).append(count);
        return sb.toString();
    }
}
