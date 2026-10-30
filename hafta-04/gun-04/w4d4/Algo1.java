package w4d4;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Algo1 {
    public static void main(String[] args) {

    }

    public static  int daysBetweenDates(String date1, String date2) {
        LocalDate firstDate = LocalDate.parse(date1);
        LocalDate secondDate = LocalDate.parse(date2);

        int days = (int) ChronoUnit.DAYS.between(firstDate, secondDate);
        return Math.abs(days);
    }
}
