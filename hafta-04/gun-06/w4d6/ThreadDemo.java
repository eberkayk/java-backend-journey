package w4d6;

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> System.out.println("Hello from " + Thread.currentThread().getName());
        Thread t = new Thread(task, "worker");

        t.run();
        t.start();
        t.join();
        System.out.println("Done in " + Thread.currentThread().getName());
    }
}
