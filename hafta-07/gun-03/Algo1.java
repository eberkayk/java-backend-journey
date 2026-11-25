import java.util.ArrayDeque;
import java.util.Objects;

public class Algo1 {
    public int calPoints(String[] operations) {
        int total = 0;
        ArrayDeque<Integer> integers = new ArrayDeque<>();
        integers.push(Integer.valueOf(operations[0]));
        for (int i = 1; i < operations.length; i++) {
            if (Objects.equals(operations[i], "C") || Objects.equals(operations[i], "D") || Objects.equals(operations[i], "+")) {
                if (Objects.equals(operations[i], "C")) {
                    integers.pop();
                }
                if (Objects.equals(operations[i], "D")) {
                    int last = integers.peek() * 2;
                    integers.push(last);
                }
                if (Objects.equals(operations[i], "+")) {
                    int last = integers.pop();
                    int last1 = integers.peek();
                    integers.push(last);
                    integers.push(last + last1);
                }
            } else {
                integers.push(Integer.valueOf(operations[i]));
            }
        }
        int x = integers.size();
        for (int i = 0; i < x; i++) {
            total += integers.pop();
        }

        return total;
    }
}
