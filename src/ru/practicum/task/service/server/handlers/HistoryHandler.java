package ru.practicum.task.service.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.Endpoint;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_HISTORY -> getHistory(exchange);
            case UNKNOWN -> sendNotAcceptable(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (path.length == 2 && path[1].equals("history") && requestMethod.equals("GET")) {
            return Endpoint.GET_HISTORY;
        } else {
            return Endpoint.UNKNOWN;
        }
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        String jsonString = gson.toJson(taskManager.getHistory());
        if (jsonString.isEmpty()) {
            sendNotFound(exchange);
        }
        writeResponse(exchange, jsonString, 200);
    }
}
