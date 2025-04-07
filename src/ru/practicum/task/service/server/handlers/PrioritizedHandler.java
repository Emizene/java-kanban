package ru.practicum.task.service.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.Endpoint;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson = getGson();

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_PRIORITIZED -> getPrioritizedTasks(exchange);
            case UNKNOWN -> sendNotAcceptable(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (path.length == 2 && path[1].equals("prioritized") && requestMethod.equals("GET")) {
            return Endpoint.GET_PRIORITIZED;
        } else {
            return Endpoint.UNKNOWN;
        }
    }

    private void getPrioritizedTasks(HttpExchange exchange) throws IOException {
        String jsonString = gson.toJson(taskManager.getPrioritizedTasks());
        if (jsonString.isEmpty()) {
            sendNotFound(exchange);
        }
        writeResponse(exchange, jsonString, 200);
    }
}
