package w5d4;

public class Algo1 {
    public static void main(String[] args) {
        System.out.println(reverseWords("Let's take LeetCode contest"));
    }

    // ahmet
    public static String reverseWords(String s) {
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            StringBuilder sb1 = new StringBuilder(words[i]);
            sb.append(sb1.reverse().toString());
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
