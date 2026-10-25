package w3d5;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CountdownIterator implements Iterator<Integer> {
    private int current;
    public CountdownIterator(int start) {
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        if (current > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Countdown finished");
        }
        return current--;
    }
}
