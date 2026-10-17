package w2d6;

public abstract class Rental {
    private final String owner;
    private final int days;
    private static int count;

    Rental(String owner, int days) {
        this.owner = owner;
        this.days = setDays(days);
        count++;
    }

    public static int getCount() {
        return count;
    }

    public String getOwner() {
        return owner;
    }

    public int getDays() {
        return days;
    }

    int setDays(int days) {
       if (days < 1) {
           return 1;
       }
       return days;
    }

    public abstract double dailyRate();

    public double totalPrice() {
        return dailyRate() * days;
    }

    @Override
    public String toString() {
        return getOwner() + " | " + getDays() + " day(s) | " + totalPrice() + " |";
    }
}
