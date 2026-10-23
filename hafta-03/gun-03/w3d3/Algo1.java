package w3d3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Algo1 {
    public static void main(String[] args) {
        int[] ints = {4, 9, 5};
        int[] ints1 = {9, 4, 9, 8, 4};
        System.out.println(Arrays.toString(intersection(ints, ints1)));
    }

    public static int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> numbers1 = new HashSet<>();
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < nums1.length; i++) {
            numbers1.add(nums1[i]);
        }

        for (int i = 0; i < nums2.length; i++) {
            if (numbers1.contains(nums2[i])) {
                result.add(nums2[i]);
            }
        }

        int[] result1 = new int[result.size()];
        int index = 0;

        for (Integer integer : result) {
            result1[index] = integer;
            index++;
        }

        return result1;
    }
}
