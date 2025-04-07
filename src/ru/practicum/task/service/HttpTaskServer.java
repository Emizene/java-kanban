package ru.practicum.task.service;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.InMemoryTaskManager;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private  static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        TaskManager newTaskManager = setUp();
        HttpTaskServer httpTaskServer = new HttpTaskServer(newTaskManager);
        httpTaskServer.start();
    }

    private static TaskManager setUp() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addTask(task1);
        return taskManager;
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }
}

