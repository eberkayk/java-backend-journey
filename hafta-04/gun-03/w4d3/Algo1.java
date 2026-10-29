package w4d3;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static int sumOfUnique(int[] nums) {
        int[] ints = new int[101];
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            ints[nums[i]]++;
        }
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == 1) {
                sum += i;
            }
        }
        return sum;
    }
}
