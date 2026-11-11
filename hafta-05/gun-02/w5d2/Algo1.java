package w5d2;

import java.util.HashMap;
import java.util.Map;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                if (k >= i - map.get(nums[i])) {
                    return true;
                }
            }
            map.put(nums[i], i);
        }
        return false;
    }
}
