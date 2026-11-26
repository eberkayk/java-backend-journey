package com.eberkayk.todoserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

// HAZIR — dokunmana gerek yok. Sunucuyu 8080 portunda başlatır.
public class TodoServerApp {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        TodoRepository repo = new InMemoryTodoRepository();
        repo.create("HTTP ogren", 1);
        repo.create("REST tasarla", 1);
        repo.create("Spring'e gec", 2);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/todos", new TodoHandler(repo));
        server.start();

        System.out.println("Sunucu calisiyor: http://localhost:" + PORT + "/todos");
    }
}
