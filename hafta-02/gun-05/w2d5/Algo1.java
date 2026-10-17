package w2d5;

import java.util.Arrays;

public class Algo1 {
    public static void main(String[] args) {
        int[] nums = {-4, -1, 0, 3, 10};
        int[] nums1 = {-8, -5, -3, -1};
        int[] nums2 = {2, 4, 5, 7};
        int[] nums3 = {3};
        int[] nums4 = {-3, -1, 1, 3};
        System.out.println(Arrays.toString(sortedSquares(nums)));
        System.out.println(Arrays.toString(sortedSquares(nums1)));
        System.out.println(Arrays.toString(sortedSquares(nums2)));
        System.out.println(Arrays.toString(sortedSquares(nums3)));
        System.out.println(Arrays.toString(sortedSquares(nums4)));
    }

//    public static int[] sortedSquares(int[] nums) {
//        for (int i = 0; i < nums.length; i++) {
//            nums[i] = nums[i] * nums[i];
//        }
//        Arrays.sort(nums);
//        return nums;
//    }

    public static  int[] sortedSquares(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int writeIndex = nums.length - 1;
        int[] sortedSquares = new int[nums.length];
        while (left <= right) {
            if (nums[left] * nums[left] > nums[right] * nums[right]) {
                sortedSquares[writeIndex] = nums[left] * nums[left];
                left++;
                writeIndex--;
            } else {
                sortedSquares[writeIndex] = nums[right] * nums[right];
                right--;
                writeIndex--;
            }
        }
        return sortedSquares;
    }
}

//**A1 — [LeetCode 977: Squares of a Sorted Array](https://leetcode.com/problems/squares-of-a-sorted-array/)** —
// iki uçtan pointer (Gün 8 A1'in kardeşi)
//        `int[] sortedSquares(int[] nums)`: `nums` küçükten büyüğe sıralı (negatif olabilir).
//        Her elemanın karesini al, sonucu **sıralı** döndür. `[-4,-1,0,3,10]` → `[0,1,9,16,100]`.
//        - Önce kolay yol: kare al + `Arrays.sort` — O(n log n). Çalıştır, LeetCode'a gönder.
//        Sonra soru: **O(n)** yapabilir misin?
//        - İpucu sorusu: sıralı dizide **en büyük kare** nerede olabilir? (Sadece iki aday var.)
//        Yeni diziyi hangi uçtan doldurmalısın?
//        - Kağıtta `[-4,-1,0,3,10]` için tablo: `left | right | yazılan index | yazılan değer`.
//        - Testler: hepsi negatif, hepsi pozitif, karışık, tek eleman.