import java.util.Arrays;

public class ArrayBasics {
    public static void main(String[] args) {
        int[] array = new int[5];
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

        String[] strings = new String[5];
        for (int i = 0; i < strings.length; i++) {
            System.out.println(strings[i]);
        }

        boolean[] booleans = new boolean[5];
        for (int i = 0; i < booleans.length; i++) {
            System.out.println(booleans[i]);
        }

        for (boolean bool : booleans) {
            System.out.println(bool);
        }

        System.out.println(Arrays.toString(booleans));
    }
}
