public class PrimesUpToN {
    public static void main(String[] args) {
        printPrimesUpTo(-5);
        System.out.println();
        printPrimesUpTo(2);
        System.out.println();
        printPrimesUpTo(7);
        System.out.println();
        printPrimesUpTo(20);
        System.out.println();
    }

    public static void printPrimesUpTo(int n) {
        if (n < 2) {
            System.out.print("Invalid entry");
            return;
        }
        for (int i = 2; i <= n; i++) {
            if (NumberUtils.isPrime(i)) {
                System.out.print(i + " ");
            }
        }
    }
}
