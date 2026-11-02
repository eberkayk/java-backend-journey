package w5d1;

public class PlaylistTools {
    public static boolean tryRemove(Playlist playlist, String title) {
        try {
            playlist.remove(title);
            return true;
        } catch (SongNotFoundException e) {
            return false;
        }
    }

    public static int parseDuration(String text) {
        if (text == null) {
            throw new InvalidSongException("Bad duration: null");
        }
        int minutes = 0;
        int seconds = 0;
        String[] strings = text.split(":");
        if (strings.length != 2) {
            throw new InvalidSongException("Bad duration: " + text);
        }
        try {
            minutes = Integer.parseInt(strings[0]);
            seconds = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            throw new InvalidSongException("Bad duration: " + text);
        }
        if (seconds < 0 || seconds > 59) {
            throw new InvalidSongException("Bad duration: " + text);
        }
        int totalSeconds = 0;
        totalSeconds += minutes * 60;
        totalSeconds += seconds;
        return totalSeconds;
    }

    public static Song parseSong(String line) {
        String[] strings = line.split(";");
        if (strings.length != 2) {
            throw new InvalidSongException("Bad line: " + line);
        }
        String title = strings[0];
        String duration = strings[1];
        int duration1 = parseDuration(duration);
        return new Song(title, duration1);
    }
}
