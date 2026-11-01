package w4d6;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public record Workout(LocalDate date, Sport sport, int minutes) {
    public Workout {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        if (sport == null) {
            throw new IllegalArgumentException("Sport must not be null");
        }
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be positive");
        }
    }

    public String toCsv() {
        return date + "," + sport.name() + "," + minutes;
    }

    public static Workout fromCsv(String line) {
        String[] splitted = line.split(",");
        LocalDate date1 = LocalDate.parse(splitted[0]);
        Sport sport1 = Sport.valueOf(splitted[1]);
        int minutes1 = Integer.parseInt(splitted[2]);
        return new Workout(date1, sport1, minutes1);
    }
}
