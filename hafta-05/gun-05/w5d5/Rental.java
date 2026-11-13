package w5d5;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Rental(String id, String customer, BikeType type, LocalDate start, LocalDate end) {

    public Rental {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id must not be blank");
        }

        if (customer == null || customer.isBlank()) {
            throw new IllegalArgumentException("Customer must not be blank");
        }

        if (type == null) {
            throw new IllegalArgumentException("Type must not be null");
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End before start");
        }
    }

    public long days() {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    public long price() {
        return days() * type().dailyPrice();
    }

    public String toCsv() {
        return id + ";" + customer + ";" + type + ";" + start + ";" + end;
    }

    public static Rental fromCsv(String line) {
        String[] vars = line.split(";");
        if (vars.length != 5) {
            throw new IllegalArgumentException("Bad line: " + line);
        }
        String id1 = vars[0];
        String customer1 = vars[1];
        BikeType type1 = BikeType.valueOf(vars[2]);
        LocalDate start1 = LocalDate.parse(vars[3]);
        LocalDate end1 = LocalDate.parse(vars[4]);
        return new Rental(id1, customer1, type1, start1, end1);
    }
}
