package w5d7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class School {
    private final List<Course> courses = new ArrayList<>();
    private final Map<String, List<Course>> studentCourses = new HashMap<>();
    private final Map<Course, Integer> courseRolledCount = new HashMap<>();
    private final Map<Course, List<String>> courseStudents = new HashMap<>();

    public void addCourse(Course c) {
        if (c == null) {
            throw new IllegalArgumentException("Course is null");
        }

        if (courses.stream().map(Course::code).anyMatch(n -> Objects.equals(n, c.code()))) {
            throw new IllegalStateException("Duplicate course: " + c.code());
        }

        courses.add(c);
        courseStudents.put(c, new ArrayList<>());
        courseRolledCount.putIfAbsent(c, 0);
    }

    public boolean enroll(String student, String code) throws CourseFullException {
        if (student == null || student.isBlank()) {
            throw new IllegalArgumentException("Student is blank");
        }

        if (courses.stream().map(Course::code).noneMatch(n -> Objects.equals(n, code))) {
            throw new NoSuchElementException("No course: " + code);
        }

        studentCourses.putIfAbsent(student, new ArrayList<>());
        if (studentCourses.get(student).stream().map(Course::code).anyMatch(n -> Objects.equals(n, code))) {
            return false;
        }
        Course course = courses.stream().filter(n -> Objects.equals(n.code(), code)).findFirst().get();
        if (courseRolledCount.get(course) >= course.capacity()) {
            throw new CourseFullException(code, course.capacity());
        }

        courseRolledCount.put(course, courseRolledCount.get(course) + 1);
        studentCourses.get(student).add(course);
        courseStudents.get(course).add(student);
        return true;
    }

    public List<String> studentsOf(String code) {
        Optional<Course> course = courses.stream().filter(n -> Objects.equals(n.code(), code)).findFirst();
        if (course.isEmpty()) {
            return List.of();
        }
        return courseStudents.get(course.get()).stream().sorted().toList();
    }

    public List<String> coursesOf(String student) {
        return studentCourses.getOrDefault(student, List.of()).stream().map(Course::code).sorted().toList();
    }

    public Optional<Course> mostPopular() {
        int most = 0;
        Course c = null;
        for (Course course : courses.stream().sorted(Comparator.comparing(Course::code)).toList()) {
            int count = courseRolledCount.get(course);
            if (count > most) {
                most = count;
                c = course;
            }
        }
        return Optional.ofNullable(c);
    }

    public Map<Level, Long> countByLevel() {
        return courses.stream().collect(Collectors.groupingBy(Course::level, TreeMap::new, Collectors.counting()));
    }

    public int freeSeats() {
        int totalCapacity = 0;
        for (Course c : courses) {
            totalCapacity += c.capacity();
            totalCapacity -= courseRolledCount.get(c);
        }

        return totalCapacity;
    }

    public void saveTo(Path file) throws IOException {
        List<String> list = new ArrayList<>();
        for (Course c : courses.stream().sorted(Comparator.comparing(Course::code)).toList()) {
            for (String s : studentsOf(c.code())) {
                list.add(c.code() + ";" + s);
            }
        }
        Files.write(file, list);
    }
}
