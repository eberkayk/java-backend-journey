package w5d2;

import java.util.Objects;

public final class Seat {
    private final char row;
    private final int number;

    public Seat(char row, int number) {
        if (row < 'A' || row > 'Z') {
            throw new IllegalArgumentException("Bad row: " + row);
        }
        if (number < 1) {
            throw new IllegalArgumentException("Bad number: " + number);
        }
        this.row = row;
        this.number = number;
    }

    public char getRow() {
        return row;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Seat)) {
            return false;
        }
        Seat o = (Seat) obj;
        return row == o.row && number == o.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, number);
    }

    @Override
    public String toString() {
        return "" + row + number;
    }
}
