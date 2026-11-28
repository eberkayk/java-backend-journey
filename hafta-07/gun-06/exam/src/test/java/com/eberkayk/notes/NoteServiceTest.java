package com.eberkayk.notes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NoteServiceTest {
    InMemoryNoteRepository repo;
    NoteService noteService;
    @BeforeEach
    void setUp() {
        repo = new InMemoryNoteRepository();
        noteService = new NoteService(repo);
    }

    @Test
    void topPriority() {
        repo.add("n1", 2);
        repo.add("n2", 5);
        repo.add("n3", 2);
        repo.add("n4", 4);
        assertEquals(2, noteService.topPriority(2).getFirst().id());
        assertEquals(4, noteService.topPriority(2).get(1).id());
    }

    @Test
    void requireWithNonExistentId() {
        assertThrows(NoteNotFoundException.class, () -> noteService.require(99));
    }
}
