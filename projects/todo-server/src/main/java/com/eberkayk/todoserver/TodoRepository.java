package com.eberkayk.todoserver;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    List<Todo> findAll();
    List<Todo> findByUser(int userId);
    Optional<Todo> findById(int id);
    Todo create(String title, int userId);
    boolean deleteById(int id);
}
