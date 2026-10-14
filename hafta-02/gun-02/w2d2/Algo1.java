package w2d2;

public class Algo1 {
    public static void main(String[] args) {
        System.out.println(maxNumberOfBalloons("nlaebolko"));
        System.out.println(maxNumberOfBalloons("loonbalxballpoon"));
        System.out.println(maxNumberOfBalloons("leetcode"));

    }

    public static int maxNumberOfBalloons(String text) {
        String balloon = "balloon";
        int balloonCount = 0;
        int[] count = new int[26];
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            count[ch - 'a']++;
        }

        while (true){
            for (int i = 0; i < balloon.length(); i++) {
                char ch = balloon.charAt(i);
                count[ch - 'a']--;
                if (count[ch - 'a'] < 0) {
                    return balloonCount;
                }
            }
            balloonCount++;
        }

    }
}


//**A1 — [LeetCode 1189: Maximum Number of Balloons](https://leetcode.com/problems/maximum-number-of-balloons/)** — sayaç dizisi, bir adım ileri
//        `maxNumberOfBalloons(String text)` → int: `text`'teki harflerle (her biri bir kez) en fazla kaç tane `"balloon"` kelimesi yazılabilir? `"nlaebolko"` → 1, `"loonbalxballpoon"` → 2, `"leetcode"` → 0.
//İpucu: Saymayı biliyorsun. Yeni olan kısım: `"balloon"`'da bazı harfler **iki kez** geçiyor. Her harf için "bu harf kaç balloon'a yeter?" diye düşün; cevabı hangi harf belirler?
