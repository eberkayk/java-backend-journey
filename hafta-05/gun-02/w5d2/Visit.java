package w5d2;

public record Visit(String person, String event) {
    public Visit{
        if (person == null || person.isBlank()) {
            throw new IllegalArgumentException("Person must not be blank");
        }

        if (event == null || event.isBlank()) {
            throw new IllegalArgumentException("Event must not be blank");
        }
    }
}
