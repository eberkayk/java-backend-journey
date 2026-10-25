package w3d4;

import java.util.Arrays;

public class Algo1 {
    public static void main(String[] args) {
        String s = "anagram";
        String t = "nagaram";
        System.out.println(isAnagram(s, t));
    }

//    public static boolean isAnagram(String s, String t) {
//        char[] chars = s.toCharArray();
//        char[] chart = t.toCharArray();
//
//        Arrays.sort(chars);
//        Arrays.sort(chart);
//
//        return Arrays.equals(chars, chart);
//    }

    public static boolean isAnagram(String s, String t) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            count[ch - 'a']++;
        }

        for (int i = 0; i < t.length(); i++) {
            char ch = t.charAt(i);
            count[ch - 'a']--;
        }

        for (int i = 0; i < count.length; i++) {
            if (count[i] != 0) {
                return false;
            }
        }

        return true;
    }
}
