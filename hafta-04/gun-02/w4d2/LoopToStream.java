package w4d2;

import java.util.ArrayList;
import java.util.List;

public class LoopToStream {
    public static void main(String[] args) {
        List<String> people = List.of("Ali", "Deniz", "Bora", "Ece", "Burak");
        System.out.println(longNamesUpperStream(people));                     // [DENIZ, BORA, BURAK]
        System.out.println(sumOfEvenSquaresStream(List.of(1, 2, 3, 4, 5, 6)));  // 56
        System.out.println(countStartingWithStream(people, "B"));             // 2
        System.out.println(countStartingWithStream(people, "Z"));             // 0
    }

    static List<String> longNamesUpper(List<String> names) {
        List<String> result = new ArrayList<>();
        for (String n : names) {
            if (n.length() > 3) {
                result.add(n.toUpperCase());
            }
        }
        return result;
    }

    public static List<String> longNamesUpperStream(List<String> names) {
        return names.stream().filter(n -> n.length() > 3).map(String::toUpperCase).toList();
    }

    static int sumOfEvenSquares(List<Integer> nums) {
        int sum = 0;
        for (int n : nums) {
            if (n % 2 == 0) {
                sum += n * n;
            }
        }
        return sum;
    }

    public static int sumOfEvenSquaresStream(List<Integer> nums) {
        return nums.stream().filter(n -> n % 2 == 0).mapToInt(n -> n * n).sum();
    }

    static long countStartingWith(List<String> names, String prefix) {
        long count = 0;
        for (String n : names) {
            if (n.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    public static long countStartingWithStream(List<String> names, String prefix) {
        return names.stream().filter(n -> n.startsWith(prefix)).count();
    }
}
