package w5d5;

public enum BikeType {
    CITY(50),
    MOUNTAIN(80),
    ELECTRIC(120);

    private final int dailyPrice;

    BikeType(int dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public int dailyPrice() {
        return dailyPrice;
    }
}
