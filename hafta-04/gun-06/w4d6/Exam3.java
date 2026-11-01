package w4d6;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Exam3 {
    public static void main(String[] args) {

    }

    public static int countWeekendDays(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("From must not be after to");
        }
        int count = 0;
        while (!from.isAfter(to)) {
            if (from.getDayOfWeek() == DayOfWeek.SATURDAY || from.getDayOfWeek() == DayOfWeek.SUNDAY) {
                count++;
            }
            from = from.plusDays(1);
        }
        return count;
    }
}
