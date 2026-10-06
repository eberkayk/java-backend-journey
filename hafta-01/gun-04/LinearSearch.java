public class LinearSearch {
    public static void main(String[] args) {
        int[] test1 = {2, 5, 3, 8, 3 ,7};
        System.out.println(indexOf(test1, 2));
        System.out.println(indexOf(test1, 7));
        System.out.println(indexOf(test1, 9));
        System.out.println(indexOf(test1, 3));
        System.out.println(contains(test1, 2));
        System.out.println(contains(test1, 7));
        System.out.println(contains(test1, 9));
        System.out.println(contains(test1, 3));

    }

    public static int indexOf(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static boolean contains(int[] arr, int target) {
        return (indexOf(arr,target) != -1);
    }
}