import java.util.Arrays;

public class Reverse {
    public static void main(String[] args) {
        int[] test1 = {1, 2, 3, 4, 5};
        int[] test2 = {1, 2, 3, 4};
        System.out.println(Arrays.toString(reversedCopy(test1)));
        System.out.println(Arrays.toString(reversedCopy(test2)));
        System.out.println(Arrays.toString(test1));
        System.out.println(Arrays.toString(test2));
        reverseInPlace(test1);
        reverseInPlace(test2);
        System.out.println(Arrays.toString(test1));
        System.out.println(Arrays.toString(test2));
    }

    public static int[] reversedCopy(int[] arr) {
        int[] reverseArr = Arrays.copyOf(arr, arr.length);
        reverseInPlace(reverseArr);
        return reverseArr;
    }

    public static void reverseInPlace(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
}
