package w2d6;

public class Exam1 {
    public static void main(String[] args) {
        Rental[] rentals = new Rental[4];
        rentals[0] = new CarRental("Ahmet", 3);
        rentals[1] = new BikeRental("Mehmet", 4);
        rentals[2] = new BikeRental("Ali", 10);
        rentals[3] = new CarRental("Veli", 0);

        for (Rental rental : rentals) {
            System.out.println(rental);
        }

        System.out.println(totalRevenue(rentals));
        System.out.println(totalInsurance(rentals));
        System.out.println(Rental.getCount());
    }

    public static double totalRevenue(Rental[] rentals) {
        double totalRevenue = 0;
        for (int i = 0; i < rentals.length; i++) {
            totalRevenue += rentals[i].totalPrice();
        }
        return totalRevenue;
    }

    public static double totalInsurance(Rental[] rentals) {
        double totalInsurance = 0;
        for (int i = 0; i < rentals.length; i++) {
            if (rentals[i] instanceof Insurable) {
                totalInsurance += ((Insurable) rentals[i]).insuranceFee();
            }
        }
        return totalInsurance;
    }
}
