package w2d3;

public class Algo2 {
    public static void main(String[] args) {
        System.out.println(removeDuplicates(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));
        System.out.println(removeDuplicates(new int[]{1, 2}));
    }
  public static int removeDuplicates(int[] nums) {
        int slow = 0;
        if (nums.length == 0) {
            return 0;
        }

        int uniqueCount = 1;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i] != nums[slow]) {
                slow++;
                nums[slow] = nums[i];
                uniqueCount++;
            }
        }
        return uniqueCount;
  }

}
//
//**A2 — [LeetCode 26: Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/)** — yavaş/hızlı iki pointer
//        `removeDuplicates(int[] nums)` → int: **sıralı** dizideki tekrarları **yerinde** sil; ilk `k` eleman benzersiz elemanlar olsun, `k`'yı döndür. `[1,1,2]` → 2, dizi `[1,2,_]`. `[0,0,1,1,1,2,2,3,3,4]` → 5, `[0,1,2,3,4,_,...]`.
//        - Yeni kalıp: iki pointer **aynı yönde** gider. Biri (hızlı) diziyi okur, diğeri (yavaş) "bir sonraki benzersiz eleman nereye yazılacak"ı gösterir.
//        - **Kağıtta** `[1,1,2]` ve `[0,0,1,1,1,2,2,3,3,4]` için her adımda iki pointer'ın değerini ve dizinin durumunu tablo olarak çiz — sonra kod.
//        - Not: "sıralı" bilgisi neden önemli? Bir cümle.