package com.eberkayk.todo;

public record Todo(int userId, int id, String title, boolean completed) {
}
