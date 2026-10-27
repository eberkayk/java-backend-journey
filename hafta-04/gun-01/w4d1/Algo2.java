package w4d1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Algo2 {
    public static void main(String[] args) {

    }

    public static int[] relativeSortArray(int[] arr1, int[] arr2) {
        Map<Integer, Integer> arr2mapped = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) {
            arr2mapped.put(arr2[i], i);
        }
        List<Integer> arr1List = new ArrayList<>();
        for (Integer i : arr1) {
            arr1List.add(i);
        }
        arr1List.sort((a,b) -> {
            if (arr2mapped.containsKey(a) && arr2mapped.containsKey(b)) {
                return Integer.compare(arr2mapped.get(a), arr2mapped.get(b));
            } else if (arr2mapped.containsKey(a)) {
                return -1;
            } else if (arr2mapped.containsKey(b)) {
                return 1;
            } else {
                return Integer.compare(a,b);
            }
        });

        int[] result = new int[arr1.length];
        int index = 0;
        for (Integer i : arr1List) {
            result[index] = i;
            index++;
        }

        return result;
    }
}
