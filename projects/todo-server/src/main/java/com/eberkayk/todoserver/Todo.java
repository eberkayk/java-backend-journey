package com.eberkayk.todoserver;

public record Todo(int userId, int id, String title, boolean completed) {
}
