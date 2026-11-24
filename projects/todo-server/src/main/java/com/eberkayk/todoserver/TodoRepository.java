package com.eberkayk.todoserver;

import java.util.*;

public class TodoRepository {
    private final Map<Integer, Todo> todos = new TreeMap<>();
    private int nextId= 1;

    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    public List<Todo> findByUser(int userId) {
        List<Todo> result = new ArrayList<>();
        for (Todo t : todos.values()) {
            if (t.userId() == userId) {
                result.add(t);
            }
        }
        return result;
    }

    public Optional<Todo> findById(int id) {
        return Optional.ofNullable(todos.get(id));
    }

    public Todo create(String title, int userId) {
        Todo todo = new Todo(userId, nextId, title, false);
        todos.put(nextId, todo);
        nextId++;
        return todo;
    }

    public boolean deleteById(int id) {
        if (todos.remove(id) == null) {
            return false;
        } else return true;
    }
}
