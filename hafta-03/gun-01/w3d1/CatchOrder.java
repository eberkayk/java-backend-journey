package w3d1;

public class CatchOrder {
    public static void main(String[] args) {
        try {
            int n = Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("NFE");
        } catch (IllegalArgumentException e) {
            System.out.println("IAE");
        }
    }
}
