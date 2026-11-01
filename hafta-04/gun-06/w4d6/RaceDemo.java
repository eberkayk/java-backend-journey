package w4d6;

import java.util.concurrent.atomic.AtomicInteger;

public class RaceDemo {
    static class Counter {
        private final AtomicInteger count = new AtomicInteger();
        public void increment() {
            count.incrementAndGet();
        }
        public int getCount() {
            return count.get();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Runnable task = () -> {
            for (int i = 0; i < 1000000; i++) {
                counter.increment();
            }
        };
        Thread t1 = new Thread(task, "w1");
        Thread t2 = new Thread(task, "w2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Count: " + counter.getCount());
    }
}
