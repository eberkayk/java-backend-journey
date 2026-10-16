package w2d3;


public class Algo1 {
    public static void main(String[] args) {
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
        System.out.println(isPalindrome("race a car"));
        System.out.println(isPalindrome(" "));
        System.out.println(isPalindrome(".,"));

    }

    public static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while (left < right) {
            while (!Character.isLetterOrDigit(s.charAt(left)) && left < right) {
                left++;
            }

            while (!Character.isLetterOrDigit(s.charAt(right)) && left < right) {
                right--;
            }

            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }
}

//**A1 — [LeetCode 125: Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)** — iki uçtan iki pointer
//        `isPalindrome(String s)` → boolean. Harf ve rakam dışındakiler yok sayılır, büyük/küçük harf farkı yok. `"A man, a plan, a canal: Panama"` → true, `"race a car"` → false, `" "` → true.
//        - Yeni araçlar: `Character.isLetterOrDigit(c)`, `Character.toLowerCase(c)`.
//        - Dünkü A2'nin (reverse string) iskeleti + yeni durum: pointer **geçersiz karaktere** denk gelirse ne yapar? Bu "atlama" sırasında pointer'lar birbirini geçebilir mi?
//        - ⚠️ Yeni String/dizi oluşturmadan çöz (önce temizleyip sonra kontrol etmek de doğru ama burada iki pointer pratiği istiyoruz).

