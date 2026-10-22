package w3d2;

import java.util.ArrayList;
import java.util.List;

public class Algo2 {
    public static void main(String[] args) {

    }

    public static List<List<Integer>> generate(int numRows) {
        List<List<Integer>> rows = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();

            for (int j = 0; j <=i; j++) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    List<Integer> prev = rows.get(i-1);
                    row.add(prev.get(j-1) + prev.get(j));
                }
            }
            rows.add(row);
        }
        return rows;
    }
}
