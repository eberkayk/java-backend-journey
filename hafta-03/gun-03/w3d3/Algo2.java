package w3d3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Algo2 {
    public static void main(String[] args) {
        int[] nums = {3,3};
        System.out.println(Arrays.toString(twoSum(nums, 6)));
    }

//    public int[] twoSum(int[] nums, int target) {
//        for(int i=0; i<nums.length - 1; i++){
//            for(int j = i + 1; j<nums.length; j++){
//                if(nums[i] + nums[j] == target){
//                    return new int[]{i,j};
//                }
//            }
//        }
//        return new int[]{};
//    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> indexes = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (indexes.containsKey(target - nums[i])) {
                return new int[]{i, indexes.get(target - nums[i])};
            }
            indexes.put(nums[i], i);
        }
        return new int[]{};
    }
}
