public class Exam2 {
    public static void main(String[] args) {
        System.out.println(isAnagram("listen", "silent"));
        System.out.println(isAnagram("aab", "abb"));
        System.out.println(isAnagram("abc", "ab"));
        System.out.println(isAnagram("", ""));

    }

    public static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()){
            return false;
        }
        int[] counts = new int[26];

        for (int i = 0; i < a.length(); i++) {
            counts[a.charAt(i) - 'a']++;
        }
        for (int i = 0; i < b.length(); i++) {
            counts[b.charAt(i) - 'a']--;
        }
        for (int count : counts) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }
}