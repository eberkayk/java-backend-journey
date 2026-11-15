package w5d7;

public record Course(String code, String title, Level level, int capacity) {
    public Course {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Code is blank");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is blank");
        }

        if (level == null) {
            throw new IllegalArgumentException("Level is null");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive: " + capacity);
        }
    }
}
