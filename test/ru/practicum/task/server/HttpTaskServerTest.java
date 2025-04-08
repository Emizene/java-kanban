package ru.practicum.task.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.HttpTaskServer;
import ru.practicum.task.service.Managers;
import ru.practicum.task.service.manager.TaskManager;
import ru.practicum.task.service.server.DurationAdapter;
import ru.practicum.task.service.server.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager taskManager;
    HttpTaskServer httpTaskServer;
    HttpClient client = HttpClient.newHttpClient();
    Task task1;
    Epic epic1;
    Subtask subtask1;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    void beforeEach() throws IOException {
        taskManager = Managers.getDefaultTaskManager();
        task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, 2, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0));
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        System.out.println(taskManager.getAllTasks());
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    void testShouldGetAll() throws IOException, InterruptedException {
        String jsonTasks = gson.toJson(taskManager.getAllTasks());
        String jsonSubtasks = gson.toJson(taskManager.getAllSubtasks());
        String jsonEpics = gson.toJson(taskManager.getAllEpics());

        URI urlTasks = URI.create("http://localhost:8080/tasks");
        URI urlSubtasks = URI.create("http://localhost:8080/subtasks");
        URI urlEpics = URI.create("http://localhost:8080/epics");

        HttpRequest taskRequest = HttpRequest.newBuilder().GET().uri(urlTasks).build();
        HttpRequest subtaskRequest = HttpRequest.newBuilder().GET().uri(urlSubtasks).build();
        HttpRequest epicRequest = HttpRequest.newBuilder().GET().uri(urlEpics).build();

        HttpResponse<String> taskResponse = client.send(taskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> subtaskResponse = client.send(subtaskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> epicResponse = client.send(epicRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, taskResponse.statusCode());
        assertEquals(200, subtaskResponse.statusCode());
        assertEquals(200, epicResponse.statusCode());

        assertEquals(jsonTasks, taskResponse.body());
        assertEquals(jsonSubtasks, subtaskResponse.body());
        assertEquals(jsonEpics, epicResponse.body());
    }

    @Test
    void testShouldGetById() throws IOException, InterruptedException {
        String jsonTask = gson.toJson(task1);
        String jsonSubtask = gson.toJson(subtask1);
        String jsonEpic = gson.toJson(epic1);

        URI urlTask = URI.create("http://localhost:8080/tasks/" + task1.getId());
        URI urlSubtasks = URI.create("http://localhost:8080/subtasks/" + subtask1.getId());
        URI urlEpics = URI.create("http://localhost:8080/epics/" + epic1.getId());

        HttpRequest taskRequest = HttpRequest.newBuilder().GET().uri(urlTask).build();
        HttpRequest subtaskRequest = HttpRequest.newBuilder().GET().uri(urlSubtasks).build();
        HttpRequest epicRequest = HttpRequest.newBuilder().GET().uri(urlEpics).build();

        HttpResponse<String> taskResponse = client.send(taskRequest,
                HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> subtaskResponse = client.send(subtaskRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> epicResponse = client.send(epicRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, taskResponse.statusCode());
        assertEquals(200, subtaskResponse.statusCode());
        assertEquals(200, epicResponse.statusCode());

        assertEquals(jsonTask, taskResponse.body());
        assertEquals(jsonSubtask, subtaskResponse.body());
        assertEquals(jsonEpic, epicResponse.body());
    }

    @Test
    void testShouldGetEpicSubtasks() throws IOException, InterruptedException {
        String jsonSubtasks = gson.toJson(taskManager.getEpicSubtasks(epic1.getId()));
        URI url = URI.create("http://localhost:8080/epics/" + epic1.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(jsonSubtasks, response.body());
    }

    @Test
    void testShouldCreateAndPostTask() throws IOException, InterruptedException {
        System.err.println(taskManager.getAllTasks());
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 4, 17, 10, 0));
        String taskJson = gson.toJson(task2);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.err.println(taskManager.getAllTasks());
        assertEquals(201, response.statusCode());
        assertEquals(2, taskManager.getAllTasks().size());
    }

    @Test
    void testShouldCreateAndPostSubtask() throws IOException, InterruptedException {
        Subtask newSubtask = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(5), LocalDateTime.of(2025, 4,
                17, 17, 0));
        String taskJson = gson.toJson(newSubtask);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(2, taskManager.getAllSubtasks().size());
        assertEquals(2, epic1.getSubtasks().size());
    }

    @Test
    void testShouldCreateAndPostEpic() throws IOException, InterruptedException {
        Epic newEpic = new Epic("NEW EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 4, 17, 16, 30));
        String taskJson = gson.toJson(newEpic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(2, taskManager.getAllEpics().size());
    }

    @Test
    void testShouldUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task(1,"UPDATE TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        String taskJson = gson.toJson(task1);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task1, taskManager.getTaskById(task1.getId()).get());
    }

    @Test
    void testShouldDeleteTask() throws IOException, InterruptedException {
        int id = task1.getId();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllTasks().size());
        assertFalse(taskManager.getTaskById(id).isPresent());
    }

    @Test
    void testShouldGetPrioritized() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(taskManager.getPrioritizedTasks());
        assertEquals(jsonString, response.body());
    }

    @Test
    void testShouldGetHistory() throws IOException, InterruptedException {
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        String jsonString = gson.toJson(taskManager.getHistory());
        assertEquals(jsonString, response.body());
    }
}
