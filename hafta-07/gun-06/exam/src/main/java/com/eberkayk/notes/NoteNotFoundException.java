package com.eberkayk.notes;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(int id) {
        super("Note not found: " + id);
    }
}
