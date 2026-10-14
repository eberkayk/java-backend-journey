package w2d2;


public class Counter {
    private static int total = 0;
    private int mine = 0;

    public void click() {
        mine++;
        total++;
    }
    public int getMine() { return mine; }
    public static int getTotal() { return total; }
}