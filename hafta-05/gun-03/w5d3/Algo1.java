package w5d3;

import java.util.Arrays;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static int heightChecker(int[] heights) {
        int[] sorted = heights.clone();
        sorted = Arrays.stream(sorted).sorted().toArray();
        int count = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != sorted[i]) {
                count++;
            }
        }
        return count;
    }
}
