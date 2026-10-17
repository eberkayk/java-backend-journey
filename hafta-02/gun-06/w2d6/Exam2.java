package w2d6;

import java.util.Arrays;

public class Exam2 {
    public static void main(String[] args) {
        int[] ints = {1, 4, 7};
        int[] ints1 = {2, 4, 9, 10};
        System.out.println(Arrays.toString(mergeSorted(ints, ints1)));
        int[] ints2 = {-6, -3, -2};
        int[] ints3 = {-8, -7, -5};
        System.out.println(Arrays.toString(mergeSorted(ints2, ints3)));
        int[] ints4 = {2};
        int[] ints5 = {4};
        System.out.println(Arrays.toString(mergeSorted(ints4, ints5)));
        int[] ints6 = {};
        int[] ints7 = {4};
        System.out.println(Arrays.toString(mergeSorted(ints6, ints7)));
    }

    public static int[] mergeSorted(int[] a, int[] b) {
        int[] merged = new int[a.length+b.length];
        int first = 0;
        int second = 0;
        int writeIndex = 0;

        while (first < a.length || second < b.length) {
            if (first < a.length && second < b.length) {
                if (a[first] < b[second]) {
                    merged[writeIndex] = a[first];
                    first++;
                } else {
                    merged[writeIndex] = b[second];
                    second++;
                }
                writeIndex++;
            } else if (first < a.length) {
                for (int i = first; first < a.length; i++) {
                    merged[writeIndex] = a[first];
                    first++;
                    writeIndex++;
                }
            } else {
                for (int i = second; second < b.length; i++) {
                    merged[writeIndex] = b[second];
                    second++;
                    writeIndex++;
                }
            }
        }
        return merged;

    }
}

