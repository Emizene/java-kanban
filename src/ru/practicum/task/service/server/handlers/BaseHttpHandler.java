package ru.practicum.task.service.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.task.service.server.DurationAdapter;
import ru.practicum.task.service.server.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    public Gson getGson() {
        return gson;
    }

    protected int getId(String pathString) {
        String[] path = pathString.split("/");
        if (path.length >= 3) {
            return Integer.parseInt(path[2]);
        }
        return -1;
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Задача не найдена", 404);
    }

    protected void sendNotAcceptable(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Неверные данные", 406);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        writeResponse(exchange, "Задача пересекается с существующей", 406);
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        OutputStream os = exchange.getResponseBody();
        byte[] response = responseString.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, response.length);
        os.write(response);
        exchange.close();
    }
}
