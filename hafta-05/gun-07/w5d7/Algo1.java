package w5d7;

import java.util.Arrays;

public class Algo1 {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(replaceElements(new int[]{17, 18, 5, 4, 6, 1})));
        System.out.println(Arrays.toString(replaceElements(new int[]{400})));
    }

    public static int[] replaceElements(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int rightMax = arr[i+1];
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] > rightMax) {
                    rightMax = arr[j];
                }
            }
            arr[i] = rightMax;
        }
        arr[arr.length - 1] = -1;
        return arr;
    }
}
