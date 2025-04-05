package ru.practicum.task.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
public class TestController {
    public final TaskManager taskManager;

    public TestController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @GetMapping
    public void fillTaskManagerWithTask() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(20),
                LocalDateTime.of(2025, 3, 17, 16, 30));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
    }

    @GetMapping("/history")
    public void fillHistoryManager() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 17, 10, 0));
        taskManager.addTask(task1);
        taskManager.getTaskById(1);
    }

    @GetMapping("/subtask")
    public void fillTaskManagerWithSubtask() {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
    }

}
