package w5d1;

public record Song(String title, int seconds) {
    public Song {
        if (title == null || title.isBlank()) {
            throw new InvalidSongException("Title must not be blank");
        }

        if (seconds <= 0) {
            throw new InvalidSongException("Seconds must be positive");
        }
    }
}
