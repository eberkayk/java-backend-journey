package w2d7;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Algo1 {
    public static void main(String[] args) {
        System.out.println(containsDuplicate(new int[]{1, 2, 3}));
        System.out.println(containsDuplicate(new int[]{1, 1, 2}));
        System.out.println(containsDuplicate(new int[]{1, 5, 2, 8, 4, 6, 5}));
        System.out.println(containsDuplicate(new int[]{1}));
    }

    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if(!seen.add(nums[i])) {
                return true;
            }
        }
        return false;
    }
}
