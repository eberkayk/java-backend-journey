package com.eberkayk.todo;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(int id) {
        super("Todo not found: " + id);
    }
}
