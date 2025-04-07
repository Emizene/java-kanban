package ru.practicum.task.service.server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.task.model.Epic;
import ru.practicum.task.service.manager.IntersectionException;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson = getGson();
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        Endpoint endpoint = getEndpoint(path, exchange.getRequestMethod());
        switch (endpoint) {
            case GET -> getAllEpics(exchange);
            case GET_BY_ID -> getEpicById(exchange, getId(path));
            case GET_EPIC_SUBTASKS_BY_ID -> getEpicSubtasks(exchange, getId(path));
            case POST -> createOrUpdateEpic(exchange);
            case DELETE_BY_ID -> deleteEpic(exchange, getId(path));
            case UNKNOWN -> sendBadRequest(exchange);
        }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        if (path[1].equals("epics") && path.length <= 4) {
            if (path.length >= 3) {
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
                    } else if (path.length == 4 && path[3].equals("subtasks")) {
                        return Endpoint.GET_EPIC_SUBTASKS_BY_ID;
                    } else if (path.length == 3) {
                        return Endpoint.GET_BY_ID;
                    } else {
                        return Endpoint.UNKNOWN;
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

    public void getAllEpics(HttpExchange exchange) throws IOException {
        String jsonString =  gson.toJson(taskManager.getAllEpics());
        if (jsonString.isEmpty()) {
            sendNotFound(exchange);
        }
        writeResponse(exchange, jsonString, 200);
    }


    public void getEpicById(HttpExchange exchange, Integer id) throws IOException {
        Optional<Epic> maybeEpic = taskManager.getEpicById(id);
        if (maybeEpic.isPresent()) {
            String jsonString = gson.toJson(maybeEpic.get());
            writeResponse(exchange, jsonString, 200);
            return;
        }
        sendNotFound(exchange);
    }

    public void getEpicSubtasks(HttpExchange exchange, Integer id) throws IOException {
        try {
            String jsonString = gson.toJson(taskManager.getEpicSubtasks(id));
            if (jsonString.isEmpty()) {
                sendNotFound(exchange);
            }
            writeResponse(exchange, jsonString, 200);
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }

    public void createOrUpdateEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String stringEpic = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        if (stringEpic.isBlank()) {
            sendNotFound(exchange);
        }
        try {
            Epic epic = gson.fromJson(stringEpic, Epic.class);
            try {
                if (epic.getId() == null) {
                    taskManager.addEpic(epic);
                    writeResponse(exchange, "Задача успешно добавлена", 201);
                } else {
                    taskManager.updateEpic(epic);
                    writeResponse(exchange, "Задача успешно обновлена", 201);
                }
            } catch (IllegalArgumentException e) {
                sendBadRequest(exchange);
            } catch (IntersectionException e) {
                sendHasInteractions(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange);
        }
    }

    public void deleteEpic(HttpExchange exchange, Integer id) throws IOException {
        try {
            taskManager.deleteEpic(id);
            writeResponse(exchange, "Задача успешно удалена", 200);
        } catch (IllegalArgumentException e) {
            sendNotFound(exchange);
        }
    }
}
