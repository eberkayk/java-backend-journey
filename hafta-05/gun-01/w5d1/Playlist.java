package w5d1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {
    private final String name;
    private final List<Song> songs = new ArrayList<>();

    public Playlist(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return songs.size();
    }

    public void add(Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song must not be null");
        }
        if (songs.contains(song)) {
            throw new IllegalStateException("Already in playlist: " + song.title());
        }
        songs.add(song);
    }

    public void insertAt(int index, Song song) {
        if (song == null) {
            throw new IllegalArgumentException("Song must not be null");
        }

        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size());
        }

        songs.add(index, song);
    }

    public Song removeAt(int index) {
        return songs.remove(index);
    }

    public void remove(String title) throws SongNotFoundException {
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(songs.get(i).title(), title)) {
                songs.remove(i);
                return;
            }
        }
        throw new SongNotFoundException("Song not found: " + title);
    }

    public int indexOf(String title) {
        for (int i = 0; i < size(); i++) {
            if (Objects.equals(songs.get(i).title(), title)) {
                return i;
            }
        }
        return -1;
    }

    public void moveUp(int index) {
        if (index <= 0 || index >= size()) {
            throw new IllegalArgumentException("Cannot move up: " + index);
        }
        Song song = songs.get(index);
        songs.add(index - 1, song);
        songs.remove(index + 1);
    }

    public int totalSeconds() {
        return songs.stream().mapToInt(Song::seconds).sum();
    }

    public List<Song> longerThan(int seconds) {
        return songs.stream().filter(n -> n.seconds() > seconds).toList();
    }

    public int removeShorterThan(int seconds) {
        int size = size();
        songs.removeIf(n -> n.seconds() < seconds);
        int newSize = size();
        return size - newSize;
    }

    public List<Song> getSongs() {
        return List.copyOf(songs);
    }
}
