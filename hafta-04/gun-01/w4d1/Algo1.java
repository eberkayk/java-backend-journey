package w4d1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Algo1 {
    public static void main(String[] args) {

    }

//    public static int[] smallerNumbersThanCurrent(int[] nums) {
//        int[] counts = new int[nums.length];
//        for (int i = 0; i < nums.length; i++) {
//            int count = 0;
//            for (int j = 0; j < nums.length; j++) {
//                if (nums[i] > nums[j]) {
//                    count++;
//                }
//            }
//            counts[i] = count;
//        }
//        return counts;
//    }

    public static int[] smallerNumbersThanCurrent(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        for (int i = 0; i < nums.length; i++) {
            map.putIfAbsent(sorted[i], i);
        }
        int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = map.get(nums[i]);
        }
        return result;
    }
}
