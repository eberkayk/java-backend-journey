public class Concat {
    public static void main(String[] args) {
        long start1 = System.currentTimeMillis();
        String s = "";
        for (int i = 0; i <= 100000; i++) {
            s += i;
        }
        long elapsed1 = System.currentTimeMillis() - start1;
        System.out.println(elapsed1);

        long start2 = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 100000; i++) {
            stringBuilder.append(i);
        }
        long elapsed2 = System.currentTimeMillis() - start2;
        System.out.println(elapsed2);
        System.out.println(elapsed1 - elapsed2);
    }
}
