package w4d2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static int findLucky(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (Integer i : arr) {
            list.add(i);
        }
        Map<Integer, Long> map = list.stream().collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
        int result = -1;
        for (Map.Entry<Integer, Long> e : map.entrySet()) {
            if (e.getKey() == e.getValue().intValue() && e.getValue().intValue() > result) {
                result = e.getKey();
            }
        }
        return result;
    }
}
