package w3d2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static List<Integer> findDisappearedNumbers(int[] nums) {
        int[] counts = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            counts[nums[i]]++;
        }
        List<Integer> numbers = new ArrayList<>();

        for (int i = 1; i < counts.length; i++) {
            if (counts[i] == 0) {
                numbers.add(i);
            }
        }
        return numbers;
    }
}
