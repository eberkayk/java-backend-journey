package w2d6;

import java.util.Arrays;

public class Exam3 {
    public static void main(String[] args) {
        System.out.println(mostFrequent("aaaabbbcccccc"));
        System.out.println(mostFrequent("abbcc"));
        System.out.println(mostFrequent("ccbba"));
        System.out.println(mostFrequent("aab"));
        System.out.println(mostFrequent(""));
    }

    public static char mostFrequent(String s) {
        if (s.length() == 0) {
            return '?'; // boş stringlerde ? döner.
        }
        int[] count = new int[26];
        int max = count[25];
        char most = 'a';
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            count[ch - 'a']++;
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (count[ch - 'a'] > max) {
                max = count[ch - 'a'];
                most = ch;
            }
        }
        return most;
    }
}

//**C3 (10 puan) — `Exam3.java`:** `static char mostFrequent(String s)` →
// en çok tekrar eden harf. Sadece küçük harf `a–z` gelir. **Eşitlikte, String'de daha önce görünen** harf kazanır.
//        `"abbcc"` → `'b'` · `"ccbba"` → `'c'` · `"aab"` → `'a'`
//        - Boş String için ne döneceğine karar ver, yorumla yaz.
//        - `main`'de en az 3 test, biri eşitlik durumu.