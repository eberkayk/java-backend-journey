package w3d6;

import java.util.*;

public class Exam2 {
    public static void main(String[] args) {
        int[] ints = {1, 1, 2, 2, 2, 3};
        int[] ints1 = {2, 3, 1, 3, 2};
        int[] ints2 = {-1, 1, -6, 4, 5, -6, 1, 4, 1};
        System.out.println(Arrays.toString(frequencySort(ints)));
        System.out.println(Arrays.toString(frequencySort(ints1)));
        System.out.println(Arrays.toString(frequencySort(ints2)));
    }

    public static int[] frequencySort(int[] nums) {
        Map<Integer, Integer> repeat = new HashMap<>();
        for (Integer i : nums) {
            repeat.put(i, repeat.getOrDefault(i, 0) + 1);
        }
        List<Integer> integers = new ArrayList<>();
        for (Integer i : nums) {
            integers.add(i);
        }
        integers.sort((a, b) -> {
            int countA = repeat.get(a);
            int countB = repeat.get(b);
            if (countA != countB) {
                return Integer.compare(countA, countB);
            }
            return Integer.compare(b, a);
        });
        int[] result = new int[integers.size()];
        int index = 0;
        for (Integer i : integers) {
            result[index] = i;
            index++;
        }
        return result;
    }
}
