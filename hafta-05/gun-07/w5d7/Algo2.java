package w5d7;

import java.util.*;

public class Algo2 {

    public static void main(String[] args) {
        System.out.println(findDifference(new int[]{1,2,3}, new int[]{2,4,6}));
    }

    public static List<List<Integer>> findDifference(int[] nums1, int[] nums2) {
        Set<Integer> set1 = new HashSet<>();
        List<Integer> list1 = new ArrayList<>();
        Set<Integer> set2 = new HashSet<>();
        List<Integer> list2 = new ArrayList<>();
        for (Integer i : nums1) {
            set1.add(i);
        }
        for (Integer i : nums2) {
            if (set1.add(i)) {
                list2.add(i);
            }
        }

        for (Integer i : nums2) {
            set2.add(i);
        }
        for (Integer i : nums1) {
            if (set2.add(i)) {
                list1.add(i);
            }
        }
        return List.of(list1, list2);
    }
}
