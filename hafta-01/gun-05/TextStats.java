public class TextStats {
    public static void main(String[] args) {
        String test1 = "Hello World 2026";
        String test2 = "";
        String test3 = "   ";
        String test4 = "aEiOu";
        System.out.println(countChar(test1, 'o'));
        System.out.println(countVowels(test1));
        System.out.println(countDigits(test1));
        System.out.println(countWords(test1));
        System.out.println();

        System.out.println(countChar(test2, 'o'));
        System.out.println(countVowels(test2));
        System.out.println(countDigits(test2));
        System.out.println(countWords(test2));
        System.out.println();

        System.out.println(countChar(test3, 'o'));
        System.out.println(countVowels(test3));
        System.out.println(countDigits(test3));
        System.out.println(countWords(test3));
        System.out.println();

        System.out.println(countChar(test4, 'o'));
        System.out.println(countVowels(test4));
        System.out.println(countDigits(test4));
        System.out.println(countWords(test4));
        System.out.println();

        System.out.println(countWords("  hello  world  "));
    }

    public static int countChar(String s, char target) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == target) {
                count++;
            }
        }
        return count;
    }

    public static int countVowels(String s) {
        int count = 0;
        char[] vowels = {'a', 'e', 'i', 'o', 'u'};
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < vowels.length; j++) {
                if (vowels[j] == s.toLowerCase().charAt(i)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int countDigits(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                count++;
            }
        }
        return count;
    }
    // "" bir string gelirse 0 döndürür, sadece boşluklardan oluşan bir string gelirse de 0 döndürür.
    public static int countWords(String s) {
        int count = 0;
        if (s.isBlank()){
            return count;
        }
        count = s.strip().split("\\s+").length;
        return count;
    }
}

//### E3 — Karakter sayma ⭐
//        `TextStats.java`:
//        - `countChar(String s, char target)` → int: target kaç kez geçiyor.
//        - `countVowels(String s)` → int: sesli harf sayısı (`a e i o u`, büyük/küçük fark etmeksizin).
//        - `countDigits(String s)` → int: `Character.isDigit` ile.
//- `countWords(String s)` → int: kelime sayısı. `"  hello   world  "` → 2 vermeli. **Boş String ve sadece boşluk** için ne dönmeli? Karar ver, yorumla yaz.
//
//        Test: `"Hello World 2026"`, `""`, `"   "`, `"aEiOu"`.