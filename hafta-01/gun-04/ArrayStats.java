public class ArrayStats {
    public static void main(String[] args) {
        int[] test1 = {3, 8, 1, 9, 4};
        int[] test2 = {-5, -2, -9};
        int[] test3 = {7};
        int[] test4 = {};

        System.out.println(sum(test1));
        System.out.println(sum(test2));
        System.out.println(sum(test3));
        System.out.println(average(test1));
        System.out.println(average(test2));
        System.out.println(average(test3));
        System.out.println(max(test1));
        System.out.println(max(test2));
        System.out.println(max(test3));
        System.out.println(min(test1));
        System.out.println(min(test2));
        System.out.println(min(test3));
        System.out.println(countGreaterThan(test1, 3));
        System.out.println(countGreaterThan(test2, -3));
        System.out.println(countGreaterThan(test3, 7));
        System.out.println(average(test4));
        System.out.println(max(test4));
        System.out.println(min(test4));
    }

    public static int sum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }
    // boş dizi gelirse 0 döndürür.
    public static double average(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int sum = sum(arr);
        return (double) sum / arr.length;
    }
    // boş dizi gelirse 0 döndürür.
    public static int max(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
    // boş dizi gelirse 0 döndürür.
    public static int min(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return min;
    }

    public static int countGreaterThan(int[] arr, int limit) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > limit) {
                count++;
            }
        }
        return count;
    }
}

