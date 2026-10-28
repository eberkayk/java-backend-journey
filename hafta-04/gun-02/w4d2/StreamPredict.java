package w4d2;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class StreamPredict {
    public static void main(String[] args) {
        List<Integer> nums = List.of(5, 3, 8, 3, 10, 1);

        System.out.println(nums.stream().filter(n -> n > 3).toList());
        System.out.println(nums.stream().map(n -> n * 2).toList());
        System.out.println(nums.stream().distinct().sorted().toList());
        System.out.println(nums.stream().filter(n -> n % 2 == 1).count());
        System.out.println(nums.stream().mapToInt(n -> n).sum());
        System.out.println(nums.stream().sorted(Comparator.reverseOrder()).limit(2).toList());
        System.out.println(nums.stream().anyMatch(n -> n > 9) + " " + nums.stream().allMatch(n -> n > 1));
        System.out.println(nums);

        Stream<Integer> once = nums.stream();
        System.out.println(once.count());
        System.out.println(once.count());
    }
}
