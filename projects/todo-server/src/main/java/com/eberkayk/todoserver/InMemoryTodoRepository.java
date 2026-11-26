package com.eberkayk.todoserver;

import java.util.*;

public class InMemoryTodoRepository implements TodoRepository {
    private final Map<Integer, Todo> todos = new TreeMap<>();
    private int nextId= 1;

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public List<Todo> findByUser(int userId) {
        List<Todo> result = new ArrayList<>();
        for (Todo t : todos.values()) {
            if (t.userId() == userId) {
                result.add(t);
            }
        }
        return result;
    }

    @Override
    public Optional<Todo> findById(int id) {
        return Optional.ofNullable(todos.get(id));
    }

    @Override
    public Todo create(String title, int userId) {
        Todo todo = new Todo(userId, nextId, title, false);
        todos.put(nextId, todo);
        nextId++;
        return todo;
    }

    @Override
    public boolean deleteById(int id) {
        return todos.remove(id) != null;
    }
}
