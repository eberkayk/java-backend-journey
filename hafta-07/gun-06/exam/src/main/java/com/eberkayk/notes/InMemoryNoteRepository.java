package com.eberkayk.notes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class InMemoryNoteRepository implements NoteRepository {
    private Map<Integer, Note> noteMap = new TreeMap<>();
    private int counter = 1;

    @Override
    public Note add(String title, int priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is blank");
        }
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Invalid priority");
        }
        Note note = new Note(counter, title, priority);
        noteMap.put(counter, note);
        counter++;
        return note;
    }

    @Override
    public Optional<Note> findById(int id) {
        return Optional.ofNullable(noteMap.get(id));
    }

    @Override
    public List<Note> findAll() {
        return List.copyOf(noteMap.values());
    }

    @Override
    public boolean delete(int id) {
        if (findById(id).isEmpty()) {
            return false;
        } else {
            noteMap.remove(id);
            return true;
        }
    }
}
