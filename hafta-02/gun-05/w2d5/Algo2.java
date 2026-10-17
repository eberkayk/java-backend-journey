package w2d5;

public class Algo2 {
    public static void main(String[] args) {
        System.out.println(longestPalindrome("aabbcc"));
        System.out.println(longestPalindrome("abbcc"));
        System.out.println(longestPalindrome("abbbcc"));
        System.out.println(longestPalindrome("Aa"));
    }

    public static int longestPalindrome(String s) {
        boolean isThereAnyOdd = false;
        int[] chars = new int[128];
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            chars[s.charAt(i)]++;
        }
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] % 2 != 0) {
                count += chars[i] - 1;
                isThereAnyOdd = true;
            } else {
                count += chars[i];
            }
        }
        if (isThereAnyOdd) {
            count++;
        }
        return count;
    }
}

//**A2 — [LeetCode 409: Longest Palindrome](https://leetcode.com/problems/longest-palindrome/)** — sayma dizisi
//        `int longestPalindrome(String s)`: `s`'nin harfleriyle (yerlerini değiştirerek) kurulabilecek
//        **en uzun palindromun uzunluğu**. Büyük/küçük harf farklı sayılır (`"Aa"` palindrom değil).
//        `"abccccdd"` → `7` (`"dccaccd"`). `"a"` → `1`.
//        - Bu sefer harf hem büyük hem küçük → `int[26]` yetmez. Kaç elemanlı bir dizi, hangi index?
//        (İpucu: `char`'ın kendisi bir sayı — Gün 5.)
//        - Düşün: palindromda bir harf kaç kere kullanılabilir? 5 tane `c` varsa kaçını kullanırsın?
//        **Tek** sayıdaki harflerden kaç tanesi ortaya gidebilir?
//        - Testler: hepsi çift sayıda, bir tek sayılı harf var, birden fazla tek sayılı harf var,
//        büyük/küçük karışık (`"Aa"` → 1).
