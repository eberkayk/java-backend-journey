package com.eberkayk.notes;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {
    Note add(String title, int priority);
    Optional<Note> findById(int id);
    List<Note> findAll();
    boolean delete(int id);
}
