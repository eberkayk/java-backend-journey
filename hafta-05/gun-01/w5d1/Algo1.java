package w5d1;

public class Algo1 {

    public static int[] sortArrayByParity(int[] nums) {
        int left = 0;
        int right = nums.length -1;
        while (left < right) {
            if (nums[left]%2==0) {
                left++;
                continue;
            }
            if (nums[right]%2==1) {
                right--;
                continue;
            }
            if (nums[left]%2!=0 && nums[right]%2!=1) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
                right--;
            }
        }
        return nums;
    }
}
