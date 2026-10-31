package w4d5;

import java.time.LocalDate;

public class Algo2 {
    public static void main(String[] args) {
        System.out.println(dayOfTheWeek(31, 8, 2019));
    }

    public static String dayOfTheWeek(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, month, day);
        String dayOfTheWeek = String.valueOf(date.getDayOfWeek());
        String result = "";
        result += dayOfTheWeek.substring(0,1);
        result += dayOfTheWeek.substring(1).toLowerCase();
        return result;
    }
}
