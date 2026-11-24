package com.eberkayk.todoserver;

public record CreateTodoRequest(String title, int userId) {
}
