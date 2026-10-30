package w4d4;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DatePredict {
    public static void main(String[] args) {
        LocalDate d = LocalDate.of(2026, 10, 30);
        System.out.println(d);                                                  // 1
        System.out.println(d.plusDays(3));                                      // 2
        d.plusDays(10);
        System.out.println(d);                                                  // 3
        System.out.println(d.getDayOfWeek());                                   // 4  (ipucu: bugün 30 Ekim 2026)
        System.out.println(d.minusMonths(8));                                   // 5  (tahmin sorusu — 30 Şubat yok, Java ne yapar?)

        LocalDate e = LocalDate.parse("2026-12-31");
        System.out.println(d.isBefore(e) + " " + e.isBefore(d));               // 6
        System.out.println(ChronoUnit.DAYS.between(d, e));                      // 7
        System.out.println(ChronoUnit.DAYS.between(e, d));                      // 8
        System.out.println(d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));// 9

        try {
            LocalDate.parse("30.10.2026");
            System.out.println("Parsed");
        } catch (DateTimeParseException ex) {
            System.out.println(ex.getClass().getSimpleName());                  // 10
        }
    }
}
