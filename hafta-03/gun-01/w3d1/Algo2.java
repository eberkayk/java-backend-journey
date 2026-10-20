package w3d1;

public class Algo2 {
    public static void main(String[] args) {
        int[] nums = {1,7,3,6,5,6};
        System.out.println(pivotIndex(nums));
    }

    public static int pivotIndex(int[] nums) {
        int sum = 0;
        int left = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        for (int i = 0; i < nums.length; i++) {
            if (left == sum - left - nums[i]) {
                return i;
            }
            left += nums[i];
        }
        return -1;
    }
}
