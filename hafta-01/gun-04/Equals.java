import java.util.Arrays;

public class Equals {
    public static void main(String[] args) {
        int[] test1 = {1, 2, 3};
        int[] test2 = {1, 2, 3};
        System.out.println(test1 == test2);
        System.out.println(Arrays.equals(test1, test2));

        int[] test3 = test1;
        System.out.println(test3 == test1);
    }
}
