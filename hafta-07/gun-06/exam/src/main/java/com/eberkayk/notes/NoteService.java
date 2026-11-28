package com.eberkayk.notes;

import java.util.Comparator;
import java.util.List;

public class NoteService {
    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public List<Note> topPriority(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid n");
        }

        if (n == 0) {
            return List.of();
        }

        return repo.findAll().stream().sorted(Comparator.comparingInt(Note::priority).reversed().thenComparing(Note::id)).limit(n).toList();
    }

    public Note require(int id) {
        if (repo.findById(id).isEmpty()) {
            throw new NoteNotFoundException(id);
        }

        return repo.findById(id).get();
    }

    public static int parsePriority(String text) {
        try {
            int result = Integer.parseInt(text);
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad priority: " + text, e);
        }
    }
}
