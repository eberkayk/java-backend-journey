import java.lang.reflect.Array;
import java.util.Arrays;

public class PassByValueArray {
    public static void main(String[] args) {
        int[] test1 = {1, 2, 3, 4};
        doubleAll(test1);
        System.out.println(Arrays.toString(test1));
        replace(test1);
        System.out.println(Arrays.toString(test1));

    }

    public static void doubleAll(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] * 2;
        }
    }
//      dönüş tipi int[] olarak
//    public static int[] replace(int[] arr) {
//        arr = new int[arr.length];
//        return arr;
//    }

    public static void replace(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
    }
}

// b=a yapınca a'nın adresini alıyor b. Arrays.copyOf() ile farklı bir adreste a'nın bir kopyası oluşturulup b'ye o adres veriliyor.