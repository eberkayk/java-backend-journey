package w2d4;

public class Algo2 {
    static void main(String[] args) {
        System.out.println(isAnagram("ahvmet", "mehmet"));
        System.out.println(isAnagram("asdzxc", "asdzxcv"));
        System.out.println(isAnagram("zxcasd", "azsxdc"));

    }

    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()){
            return false;
        }
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            count[ch - 'a']++;
            char cha = t.charAt(i);
            count[cha - 'a']--;
        }
        for (int i : count) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }
}

//**A2 — [LeetCode 242: Valid Anagram](https://leetcode.com/problems/valid-anagram/)**
// — sayma dizisi (Gün 6 A2'nin kardeşi)
//        `boolean isAnagram(String s, String t)`: `t`, `s`'nin harflerinin yer değiştirilmiş hâli mi?
//        `"anagram"`, `"nagaram"` → true. `"rat"`, `"car"` → false. Sadece küçük harf.
//        - Önce: uzunluklar farklıysa? (Bir satır.)
//        - Bir tane `int[26]` ile çözebilir misin (iki tane değil)? İpucu: biri artırır, öbürü azaltır —
//        sonunda dizide ne görmeyi beklersin?
//        - En az 3 test, biri farklı uzunluklu.