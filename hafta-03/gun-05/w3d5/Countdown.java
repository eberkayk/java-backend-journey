package w3d5;

import java.util.Iterator;

public class Countdown implements Iterable<Integer> {
    private final int start;
    public Countdown(int start) {
        if (start < 0) {
            throw new IllegalArgumentException("Start must not be negative");
        }
        this.start = start;
    }

    public Iterator<Integer> iterator() {
        return new CountdownIterator(start);
    }
}
