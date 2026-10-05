public class PassByValue {
    public static void main(String[] args) {
        int a = 5;
        int b = 8;
        swap(a,b);
        System.out.println(a);
        System.out.println(b);
        int total = 10;
        total = addFive(total);
        System.out.println(total);

    }
    public static void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }

    public static int addFive(int number) {
        return number + 5;
    }
}
