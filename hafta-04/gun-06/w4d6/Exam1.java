package w4d6;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

public class Exam1 {
    public static void main(String[] args) throws IOException {
        WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.add(new Workout(LocalDate.of(2026,10,26), Sport.RUN, 30));
        workoutLog.add(new Workout(LocalDate.of(2026,10,26), Sport.SWIM, 45));
        workoutLog.add(new Workout(LocalDate.of(2026, 10, 28), Sport.BIKE, 60));
        workoutLog.add(new Workout(LocalDate.of(2026, 10, 30), Sport.RUN, 40));
        workoutLog.add(new Workout(LocalDate.of(2026, 11, 1), Sport.BIKE, 50));

        try {
            new Workout(LocalDate.of(2026,10,31),Sport.RUN, 0);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            Workout.fromCsv("2026-10-31,YOGA,20");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Total: " + workoutLog.totalMinutes());
        workoutLog.bySport(Sport.RUN).forEach(System.out::println);
        System.out.println("Longest: " + workoutLog.longest());
        System.out.println("Longest in empty log: " + new WorkoutLog().longest());
        System.out.println(workoutLog.minutesBySport());
        System.out.println("Active days: " + workoutLog.activeDays(LocalDate.of(2026,10,26), LocalDate.of(2026,10,30)));
        System.out.println("Active days: " + workoutLog.activeDays(LocalDate.of(2026,10,27), LocalDate.of(2026,10,31)));

        try {
            workoutLog.activeDays(LocalDate.of(2026,11,1), LocalDate.of(2026,10,01));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        Path path = Path.of("workouts.csv");
        workoutLog.saveTo(path);
        System.out.println("Saved " + workoutLog.size() + " workouts");
        WorkoutLog workoutLog1 = WorkoutLog.loadFrom(path);
        System.out.println("Loaded " + workoutLog1.size() + " workouts, total " + workoutLog1.totalMinutes());

    }
}
