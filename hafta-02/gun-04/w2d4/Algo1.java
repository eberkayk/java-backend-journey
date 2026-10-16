package w2d4;

import java.util.Arrays;

public class Algo1 {
    public static void main(String[] args) {
        int[] nums = {0, 1, 0, 3, 12};
        moveZeroes(nums);
        System.out.println(Arrays.toString(nums));
        int[] nums2 = {0};
        int[] nums3 = {1, 2, 3};
        moveZeroes(nums2);
        moveZeroes(nums3);
        System.out.println(Arrays.toString(nums2));
        System.out.println(Arrays.toString(nums3));
    }


    public static  void moveZeroes(int[] nums) {
        int slow = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[slow] = nums[i];
                slow++;
            }
        }
        for (int i = slow; i < nums.length; i++) {
            nums[i] = 0;
        }
    }
//    public static void moveZeroes(int[] nums) {
//        int left = 0;
//        int right = nums.length - 1;
//
//        while (left < right) {
//            if (nums[left] == 0) {
//                for (int i = left; i < right; i++) {
//                    nums[i] = nums[i+1];
//                }
//                nums[right] = 0;
//                right--;
//            } else {
//                left++;
//            }
//        }
//    }
}

//**A1 — [LeetCode 283: Move Zeroes](https://leetcode.com/problems/move-zeroes/)** — yavaş/hızlı pointer (dünkü A2'nin kardeşi)
//        `void moveZeroes(int[] nums)`: bütün `0`'ları sona taşı, sıfır olmayanların **sırası bozulmasın**, **yerinde** (yeni dizi yok). `[0,1,0,3,12]` → `[1,3,12,0,0]`. `[0]` → `[0]`.
//        - Dünkü kalıp: hızlı okur, yavaş "sıfır olmayan bir sonraki eleman nereye yazılacak"ı gösterir. Yeni soru: döngü bitince yavaş pointer'dan sonrası ne olmalı?
//        - **Kağıtta** `[0,1,0,3,12]` için her adımda iki pointer'ı ve dizinin durumunu tablo olarak çiz.
//        - Metot `void` → test ederken **dizinin tamamını** yazdır (`Arrays.toString(nums)`). En az 3 test: örnek, `[0]`, hiç sıfır olmayan bir dizi (dünkü bug'ı hatırla).
