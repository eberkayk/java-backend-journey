package w4d3;

public class Algo2 {
    public static void main(String[] args) {

    }

//    public static int numIdenticalPairs(int[] nums) {
//        int count = 0;
//        for (int i = 0; i < nums.length; i++) {
//            for (int j = 0; j < nums.length; j++) {
//                if (nums[i] == nums[j] && i < j) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }

    public static int numIdenticalPairs(int[] nums) {
        int[] count = new int[101];
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            count[nums[i]]++;
        }
        for (int i = 0; i < count.length; i++) {
            sum += count[i] * (count[i] - 1) / 2 ;
        }
        return sum;
    }
}
