package w5d7;

public class CourseFullException extends Exception {
    private final String code;
    private final int capacity;
    public CourseFullException(String code, int capacity) {
        super("Course full: " + code + " (capacity " + capacity + ")");
        this.code = code;
        this.capacity = capacity;
    }

    public String getCode() {
        return code;
    }

    public int getCapacity() {
        return capacity;
    }
}
