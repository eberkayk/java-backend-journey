package com.eberkayk.todo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class TodoClient {
    private final String baseUrl;
    private final HttpClient http;
    private final ObjectMapper mapper;

    public TodoClient(String baseUrl) {
        this.baseUrl = baseUrl;
        http = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
    }

    public Todo findById(int id) throws IOException, InterruptedException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/todos/" + id))
                .GET()
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Todo todo = mapper.readValue(response.body(), Todo.class);
            return todo;
        }

        if (response.statusCode() == 404) {
            throw new TodoNotFoundException(id);
        } else {
            throw new IllegalStateException("Unexpected status: " + response.statusCode());
        }
    }

    public List<Todo> findByUser(int userId) throws IOException, InterruptedException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId");
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/todos?userId=" + userId))
                .GET()
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<Todo> todos = mapper.readValue(response.body(), new TypeReference<List<Todo>>() {});
            return todos;
        } else {
            throw new IllegalStateException("Unexpected status: " + response.statusCode());
        }
    }

    public Todo create(String title, int userId) throws IOException, InterruptedException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is blank");
        }

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId");
        }
        String json = mapper.writeValueAsString(Map.of("title", title, "userId", userId, "completed", false));
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/todos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            Todo todo = mapper.readValue(response.body(), Todo.class);
            return todo;
        } else {
            throw new IllegalStateException("Unexpected status: " + response.statusCode());
        }
    }
}
