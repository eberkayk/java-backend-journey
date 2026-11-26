package com.eberkayk.todoserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTodoRepositoryTest {
    private TodoRepository repo;
    @BeforeEach
    void setUp() {
        repo = new InMemoryTodoRepository();
    }

    @Test
    void create_assignsIncreasingIds() {
        Todo a = repo.create("A", 1);
        Todo b = repo.create("B", 1);
        assertEquals(1, a.id());
        assertEquals(2, b.id());
        assertFalse(a.completed());
    }

    @Test
    void findById_missingId_returnsEmpty() {
        assertTrue(repo.findById(99).isEmpty());
    }

    @Test
    void findByUser_returnsOnlyThatUsersTodos() {
        Todo a = repo.create("A", 1);
        Todo b = repo.create("B", 2);
        Todo c = repo.create("C", 1);
        assertEquals(2, repo.findByUser(1).size());
        assertEquals("A", repo.findByUser(1).getFirst().title());
        assertEquals("C", repo.findByUser(1).get(1).title());
    }

    @Test
    void deleteById_existing_removesIt() {
        Todo a = repo.create("A", 1);
        assertTrue(repo.deleteById(a.id()));
        assertTrue(repo.findById(a.id()).isEmpty());
    }
}
