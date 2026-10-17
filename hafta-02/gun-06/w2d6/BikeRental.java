package w2d6;

public class BikeRental extends Rental {
    BikeRental(String owner, int days) {
        super(owner, days);
    }

    @Override
    public double dailyRate() {
        return 40;
    }

    @Override
    public double totalPrice() {
        if (getDays() > 6) {
            return super.totalPrice() * 0.8;
        } else {
            return super.totalPrice();
        }
    }
}
