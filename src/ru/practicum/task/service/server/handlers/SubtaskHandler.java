package ru.practicum.task.service.server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.service.manager.IntersectionException;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson = getGson();
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());
        switch (endpoint) {
            case GET -> getSubtasks(exchange);
            case GET_BY_ID -> getSubtaskById(exchange, getId(path));
            case POST -> createOrUpdateSubtask(exchange);
            case DELETE_BY_ID -> deleteSubtask(exchange, getId(path));
            case UNKNOWN -> sendNotAcceptable(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (path[1].equals("subtasks") && path.length <= 3) {
            if (path.length == 3) {
                try {
                    Integer.parseInt(path[2]);
                } catch (NumberFormatException e) {
                    return Endpoint.UNKNOWN;
                }
            }
            switch (requestMethod) {
                case "GET":
                    if (path.length == 2) {
                        return Endpoint.GET;
                    } else {
                        return Endpoint.GET_BY_ID;
                    }
                case "POST":
                    return Endpoint.POST;
                case "DELETE":
                    return Endpoint.DELETE_BY_ID;
                default:
                    return Endpoint.UNKNOWN;
            }
        }
        return Endpoint.UNKNOWN;
    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        String jsonString = gson.toJson(taskManager.getAllSubtasks());
        if (jsonString.isEmpty()) {
            sendNotFound(exchange);
        }
        writeResponse(exchange, jsonString, 200);
    }

    private void getSubtaskById(HttpExchange exchange, Integer id) throws IOException {
        Optional<Subtask> maybeSubtask = taskManager.getSubtaskById(id);
        if (maybeSubtask.isPresent()) {
            String jsonString = gson.toJson(maybeSubtask.get());
            writeResponse(exchange, jsonString, 200);
            return;
        }
        sendNotFound(exchange);
    }

    private void createOrUpdateSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String stringSubtask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        if (stringSubtask.isBlank()) {
            sendNotFound(exchange);
        }
        try {
            Subtask subtask = gson.fromJson(stringSubtask, Subtask.class);
            try {
                if (subtask.getId() == null) {
                    taskManager.addSubtask(subtask);
                    writeResponse(exchange, "Задача успешно добавлена", 201);
                } else {
                    taskManager.updateSubtask(subtask);
                    writeResponse(exchange, "Задача успешно обновлена", 201);
                }
            } catch (IllegalArgumentException e) {
                sendNotAcceptable(exchange);
            } catch (IntersectionException e) {
                sendHasInteractions(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendNotAcceptable(exchange);
        }
    }

    private void deleteSubtask(HttpExchange exchange, Integer id) throws IOException {
        try {
            taskManager.deleteSubtask(id);
            writeResponse(exchange, "Задача успешно удалена", 200);
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

}
