package w5d3;

import java.time.LocalDate;
import java.util.Comparator;

public record Task(String title, int priority, LocalDate due) implements Comparable<Task> {
    public Task{
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }

        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Bad priority: " + priority);
        }

        if (due == null) {
            throw new IllegalArgumentException("Due must not be null");
        }
    }

    public int compareTo(Task other) {
        int c = this.due.compareTo(other.due);
        if (c != 0) {
            return c;
        }
        return this.title.compareTo(other.title);
    }
}
