package w3d4;

import java.util.*;

public class Algo2 {
    public static void main(String[] args) {

    }

    public static String[] sortPeople(String[] names, int[] heights) {
        Map<Integer, String> sorted = new HashMap<>();

        for (int i = 0; i < names.length; i++) {
            sorted.put(heights[i], names[i]);
        }

        Arrays.sort(heights);

        String[] result = new String[names.length];
        int index = 0;
        for (int i = names.length - 1; i >= 0; i--) {
            result[index] = sorted.get(heights[i]);
            index++;
        }

        return result;
    }
}
