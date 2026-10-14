package w2d2;

import java.util.Arrays;

public class Algo2 {
    public static void main(String[] args) {
        char[] chars = {'a','b','c','d'};
        reverseString(chars);
        System.out.println(Arrays.toString(chars));

        char[] chars1 = {'h','e','l','l','o'};
        reverseString(chars1);
        System.out.println(Arrays.toString(chars1));

    }

    public static void reverseString(char[] s) {
        int left = 0;
        int right = s.length - 1;
        while (left < right) {
            char temp = s[right];
            s[right] = s[left];
            s[left] = temp;
            left++;
            right--;
        }
    }
}

//**A2 — [LeetCode 344: Reverse String](https://leetcode.com/problems/reverse-string/)** — iki pointer'a giriş
//        `reverseString(char[] s)` → void: diziyi **yerinde** (yeni dizi oluşturmadan) ters çevir. `['h','e','l','l','o']` → `['o','l','l','e','h']`.
//İpucu: Biri baştan, biri sondan başlayan iki index. Her adımda ne yaparlar, ne zaman dururlar? **Döngü koşulunu kağıtta 4 ve 5 elemanlı iki dizi için izle** (off-by-one listende).
//Not: Burada dizinin içini değiştirmek **istenen şey** — Exam1'deki "girdiyi bozma" hatasıyla karıştırma; fark, soru bunu istiyor mu istemiyor mu.
