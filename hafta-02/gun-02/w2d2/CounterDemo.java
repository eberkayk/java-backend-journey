package w2d2;

public class CounterDemo {
    public static void main(String[] args) {
        Counter a = new Counter();
        Counter b = new Counter();
        a.click(); a.click(); b.click();
        System.out.println(a.getMine() + " " + b.getMine() + " " + Counter.getTotal());   // 1
        Counter c = a;
        c.click();
        System.out.println(a.getMine() + " " + c.getMine() + " " + Counter.getTotal());   // 2
        Counter d = new Counter();
        System.out.println(d.getMine() + " " + Counter.getTotal());                       // 3
    }
}
