package com.eberkayk.todoserver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class TodoHandler implements HttpHandler {

    private final TodoRepository repo;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public TodoHandler(TodoRepository repo) {
        this.repo = repo;
    }

    // =====================================================================
    // ROUTER (HAZIR) — gelen isteği METOD + YOL'a göre doğru metoda yollar.
    // Spring'de bu işi @GetMapping / @PostMapping / @DeleteMapping yapacak.
    // =====================================================================
    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            String method = ex.getRequestMethod();          // "GET", "POST", ...
            String path = ex.getRequestURI().getPath();     // "/todos" veya "/todos/5"
            String[] parts = path.split("/");               // ["", "todos"] veya ["", "todos", "5"]

            if (parts.length == 2 && method.equals("GET")) {
                handleList(ex);
            } else if (parts.length == 2 && method.equals("POST")) {
                handleCreate(ex);
            } else if (parts.length == 3 && method.equals("GET")) {
                handleGetOne(ex, parts[2]);
            } else if (parts.length == 3 && method.equals("DELETE")) {
                handleDelete(ex, parts[2]);
            } else if (parts.length == 2 || parts.length == 3) {
                sendError(ex, 405, "Method not allowed");
            } else {
                sendError(ex, 404, "Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();                            // sunucu loguna detay
            if (ex.getResponseCode() == -1) {               // cevap henüz gönderilmediyse
                sendError(ex, 500, "Internal error");       // istemciye detay sızdırma
            }
        } finally {
            ex.close();
        }
    }

    // =====================================================================
    // SENİN KISMIN — DERS.md Görev B3'teki koşullara göre doldur.
    // =====================================================================

    // GET /todos  ve  GET /todos?userId=3
    private void handleList(HttpExchange ex) throws IOException {
        String u = queryParam(ex, "userId");
        if (u == null) {
            sendJson(ex, 200, repo.findAll());
            return;
        }

        int x = parsePositiveInt(u);
        if (x == -1) {
            sendError(ex, 400, "Invalid userId");
            return;
        }
        sendJson(ex, 200, repo.findByUser(x));
    }

    // GET /todos/{id}
    private void handleGetOne(HttpExchange ex, String idText) throws IOException {
        int idTextInteger = parsePositiveInt(idText);
        if (idTextInteger == -1) {
            sendError(ex, 400, "Invalid id");
            return;
        }
        Optional<Todo> optionalTodo = repo.findById(idTextInteger);
        if (optionalTodo.isEmpty()) {
            sendError(ex, 404, "Todo not found: " + idTextInteger);
            return;
        }

        if (optionalTodo.isPresent()) {
            sendJson(ex, 200, optionalTodo.get());
            return;
        }
    }

    // POST /todos   body: {"title": "...", "userId": 1}
    private void handleCreate(HttpExchange ex) throws IOException {
        try {
            CreateTodoRequest request = readBody(ex, CreateTodoRequest.class);
            if (request.title() == null || request.title().isBlank()) {
                sendError(ex, 400, "Title is blank");
                return;
            }
            if (request.userId() <= 0) {
                sendError(ex, 400, "Invalid userId");
                return;
            }
            Todo todo = repo.create(request.title(), request.userId());
            ex.getResponseHeaders().set("Location", "/todos/" + todo.id());
            sendJson(ex, 201, todo);
        } catch (IOException e) {
            sendError(ex, 400, "Invalid JSON");
        }
    }

    // DELETE /todos/{id}
    private void handleDelete(HttpExchange ex, String idText) throws IOException {
        int idTextInteger = parsePositiveInt(idText);
        if (idTextInteger == -1) {
            sendError(ex, 400, "Invalid id");
            return;
        }
        if (repo.deleteById(idTextInteger)) {
            sendNoContent(ex);
        } else {
            sendError(ex, 404, "Todo not found: " + idTextInteger);
        }
        return;
    }

    // "5" → 5 ;  null, "", "abc", "0", "-3" → -1
    private int parsePositiveInt(String text) {
        if (text == null || text.isBlank()) {
            return -1;
        }
        try {
            int x = Integer.parseInt(text);
            if (x <= 0) {
                return -1;
            } else {
                return x;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // =====================================================================
    // YARDIMCILAR (HAZIR) — cevap göndermek / istek okumak için bunları kullan.
    // =====================================================================

    // status + JSON body gönderir. body: Todo, List<Todo>, Map... ne verirsen JSON'a çevrilir.
    private void sendJson(HttpExchange ex, int status, Object body) throws IOException {
        byte[] bytes = mapper.writeValueAsBytes(body);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(status, bytes.length);
        ex.getResponseBody().write(bytes);
    }

    // status + {"error": "mesaj"} gönderir.
    private void sendError(HttpExchange ex, int status, String message) throws IOException {
        sendJson(ex, status, Map.of("error", message));
    }

    // 204 No Content: body'siz cevap.
    private void sendNoContent(HttpExchange ex) throws IOException {
        ex.sendResponseHeaders(204, -1);
    }

    // /todos?userId=3  →  queryParam(ex, "userId") = "3" ;  parametre yoksa null
    private String queryParam(HttpExchange ex, String name) {
        String query = ex.getRequestURI().getQuery();
        if (query == null) {
            return null;
        }
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv[0].equals(name)) {
                return kv.length == 2 ? kv[1] : "";
            }
        }
        return null;
    }

    // İstek body'sindeki JSON'u verilen tipe çevirir.
    // JSON bozuksa / boşsa JsonProcessingException fırlatır.
    private <T> T readBody(HttpExchange ex, Class<T> type) throws IOException {
        return mapper.readValue(ex.getRequestBody(), type);
    }
}
