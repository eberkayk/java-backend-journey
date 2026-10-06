import java.lang.reflect.Array;
import java.util.Arrays;

public class Matrix {
    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3, 5},
                {3, 7, 12, 54},
                {43, 102, 96, -432},
                {1, 2}
        };

        printMatrix(matrix);
        System.out.println(Arrays.toString(rowSums(matrix)));
    }

    public static void printMatrix(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.printf("%6d", arr[i][j]);
            }
            System.out.println();
        }
    }

    public static int[] rowSums(int[][] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            int sum = 0;
            for (int j = 0; j < arr[i].length; j++) {
                sum += arr[i][j];
            }
            result[i] = sum;
        }
        return result;
    }
}

//### E8 — Matrix (2D, kısa)
//`Matrix.java`: 3×4 bir `int[][]` oluştur (değerleri sen seç).
//        - `printMatrix(int[][] m)` → void: satır satır, düzgün hizalı (`printf`, Gün 2).
//        - `rowSums(int[][] m)` → `int[]`: her satırın toplamı.
//- Döngü sınırlarında `m.length` ve `m[i].length`'i doğru yerde kullandığından emin ol. **Hangisi satır, hangisi sütun?** Yorum satırıyla yaz.
