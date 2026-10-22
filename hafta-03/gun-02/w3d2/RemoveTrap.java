package w3d2;

import java.util.ArrayList;
import java.util.List;

public class RemoveTrap {
    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 40));
        nums.remove(1);
        System.out.println(nums);                 // 1
        nums.remove(Integer.valueOf(40));
        System.out.println(nums);                 // 2
        nums.remove(30);
        System.out.println(nums);                 // 3
    }
}
