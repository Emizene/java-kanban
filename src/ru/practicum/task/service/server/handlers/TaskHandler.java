package ru.practicum.task.service.server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.IntersectionException;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson = getGson();
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());
        switch (endpoint) {
            case GET -> getAllTasks(exchange);
            case GET_BY_ID -> getTaskById(exchange, getId(path));
            case POST -> createOrUpdateTask(exchange);
            case DELETE_BY_ID -> deleteTask(exchange, getId(path));
            case UNKNOWN -> sendBadRequest(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (path[1].equals("tasks") && path.length <= 3) {
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

    private void getAllTasks(HttpExchange exchange) throws IOException {
        String jsonString = gson.toJson(taskManager.getAllTasks());
        if (jsonString.isEmpty()) {
            sendNotFound(exchange);
        }
        writeResponse(exchange, jsonString, 200);
    }

    private void getTaskById(HttpExchange exchange, Integer id) throws IOException {
        Optional<Task> task = taskManager.getTaskById(id);
        if (task.isPresent()) {
            String jsonString = gson.toJson(task.get());
            writeResponse(exchange, jsonString, 200);
            return;
        }
        sendNotFound(exchange);
    }

    private void createOrUpdateTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String stringTask = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        if (stringTask.isBlank()) {
            sendNotFound(exchange);
        }
        try {
            Task task = gson.fromJson(stringTask, Task.class);
            try {
                if (task.getId() == null) {
                    taskManager.addTask(task);
                    writeResponse(exchange, "Задача успешно добавлена", 201);
                } else {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача успешно обновлена", 201);
                }
            } catch (IntersectionException e) {
                sendHasInteractions(exchange);
            } catch (IllegalArgumentException e) {
                sendBadRequest(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange);
        }
    }

    private void deleteTask(HttpExchange exchange, Integer id) throws IOException {
        try {
            taskManager.deleteTask(id);
            writeResponse(exchange, "Задача успешно удалена", 200);
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }
}
