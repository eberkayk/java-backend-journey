import java.util.Arrays;

public class Exam1 {
    public static void main(String[] args) {
        int[] arr = {5, 1, 5, 3};
        int[] arr2 = {-1, -2, -3};
        int[] arr3 = {7};
        int[] arr4 = {4, 4, 4};
        System.out.println(secondLargest(arr));
        System.out.println(secondLargest(arr2));
        System.out.println(secondLargest(arr3));
        System.out.println(secondLargest(arr4));
        System.out.println(Arrays.toString(arr));

    }
    // dizi boşsa, bütün değerleri aynıysa ve tek elemanlıysa Integer.MIN_VALUE döndürür.
    public static int secondLargest(int[] arr) {
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;

        for (int x : arr) {
            if (largest < x) {
                secondLargest = largest;
                largest = x;
            } else if (largest > x && x > secondLargest) {
                secondLargest = x;
            }
        }
        return secondLargest;
    }
}
