package w4d6;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class WorkoutLog {
    private final List<Workout> workouts = new ArrayList<>();

    public void add(Workout w) {
        if (w == null) {
            throw new IllegalArgumentException("Workout must not be null");
        }
        workouts.add(w);
    }

    public int size() {
        return workouts.size();
    }

    public int totalMinutes() {
        return workouts.stream().mapToInt(Workout::minutes).sum();
    }

    public List<Workout> bySport(Sport sport) {
        return workouts.stream().filter(n -> n.sport() == sport).sorted(Comparator.comparing(Workout::date)).toList();
    }

    public Map<Sport, Integer> minutesBySport() {
        return new TreeMap<>(workouts.stream().collect(Collectors.groupingBy(Workout::sport, Collectors.summingInt(Workout::minutes))));
    }

    public long activeDays(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From must not be after to");
        }

        Set<LocalDate> workoutsSet = workouts.stream().map(Workout::date).
                filter(date -> date.isAfter(from.minusDays(1))).filter(date -> date.isBefore(to.plusDays(1))).collect(Collectors.toSet());

        return workoutsSet.size();
    }

    public void saveTo(Path path) throws IOException {
        Files.write(path, workouts.stream().map(Workout::toCsv).toList());
    }

    public static WorkoutLog loadFrom(Path path) throws IOException {
        List<String> strings = Files.readAllLines(path);
        WorkoutLog workoutLog = new WorkoutLog();
        for (String s: strings) {
            workoutLog.add(Workout.fromCsv(s));
        }
        return workoutLog;
    }

    public Optional<Workout> longest() {
        return workouts.stream().max(Comparator.comparing(Workout::minutes));
    }
}
