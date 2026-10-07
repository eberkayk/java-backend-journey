public class Palindrome {
    public static void main(String[] args) {
        System.out.println(reverse("Hello"));
        System.out.println(reverseWithStringBuilder("Hello"));
        System.out.println(isPalindrome("racecar"));
        System.out.println(isPalindrome("mAnam"));
        System.out.println(isPalindromeIgnoreCase("mAnam"));
        System.out.println(isSentencePalindrome("A man, a plan, a canal: Panama"));
        System.out.println(isSentencePalindrome("Amanama"));
        System.out.println(isSentencePalindrome("race a car"));

    }

    public static String reverse(String s) {
        String reverse = "";
        for (int i = s.length()-1; i >= 0; i--) {
            reverse += s.charAt(i);
        }
        return reverse;
    }

    public static String reverseWithStringBuilder(String s) {
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }

    public static boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isPalindromeIgnoreCase(String s) {
        int left = 0;
        int right = s.length() - 1;
        s = s.toLowerCase();
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static boolean isSentencePalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        s = s.toLowerCase();
        while (left < right) {
            char leftChar = s.charAt(left);
            char rightChar = s.charAt(right);
            if (!Character.isLetterOrDigit(leftChar)) {
                left++;
                continue;
            }
            if (!Character.isLetterOrDigit(rightChar)) {
                right--;
                continue;
            }
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

}
