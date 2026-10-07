public class Caesar {
    public static void main(String[] args) {
        System.out.println(encrypt("hello!", 1));
        System.out.println(decrypt("ifmmp", 1));
        System.out.println(encrypt("abc", 29));
        System.out.println(decrypt("def", 29));

    }

    public static String encrypt(String s, int shift) {
        shift = shift % 26;
        String encrytped = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                int pos = c - 'a';
                int newPos = (pos + shift) % 26;
                encrytped += (char) ('a' + newPos);
            } else if (Character.isUpperCase(c)) {
                int pos = c - 'A';
                int newPos = (pos + shift) % 26;
                encrytped += (char) ('A' + newPos);
            } else {
                encrytped += c;
            }


        }
        return encrytped;
    }

    public static String decrypt(String s, int shift) {
        return encrypt(s, 26 - (shift % 26));
    }
}
