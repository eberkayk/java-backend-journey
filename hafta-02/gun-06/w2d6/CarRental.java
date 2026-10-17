package w2d6;

public class CarRental extends Rental implements Insurable{
    CarRental(String owner, int days) {
        super(owner, days);
    }

    @Override
    public double dailyRate() {
        return 150;
    }

    @Override
    public double insuranceFee() {
        return 30 * getDays();
    }
}
