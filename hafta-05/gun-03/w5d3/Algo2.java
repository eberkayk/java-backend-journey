package w5d3;

import java.util.Arrays;

public class Algo2 {
    public static void main(String[] args) {

    }

    public int[] kWeakestRows(int[][] mat, int k) {
        int[] sort = new int[mat.length];
        for (int i = 0; i < mat.length; i++) {
            int sum = 0;
            for (int j = 0; j < mat[i].length; j++) {
                sum += mat[i][j];
            }
            sort[i] = sum;
        }
        Integer[] idx = new Integer[mat.length];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
        Arrays.sort(idx, (a, b) -> {
            if (sort[a] != sort[b]) {
                return Integer.compare(sort[a], sort[b]);
            } else {
                return Integer.compare(a, b);
            }
        });
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = idx[i];
        }
        return result;
    }
}
