package ru.practicum.task;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.model.Status;
import ru.practicum.task.service.manager.Managers;
import ru.practicum.task.service.manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefaultTaskManager();

        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2025, 3, 19, 10, 0));
        Task task3 = new Task("TASK №3", "DESCRIPTION №3", Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2025, 3, 19, 10, 0));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Epic epic2 = new Epic("EPIC №2", "DESCRIPTION №2", Status.NEW,
                LocalDateTime.of(2025, 3, 19, 19, 19));
        Epic epic3 = new Epic("EPIC №3", "DESCRIPTION №3", Status.DONE,
                LocalDateTime.of(2025, 3, 19, 20, 15));

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 3, 17, 17, 0));
        Subtask subtask2 = new Subtask("SUBTASK №2 EPIC №2 ", "DESCRIPTION №2",
                Status.NEW, epic1.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 19, 20, 0));
        Subtask subtask3 = new Subtask("SUBTASK №3 EPIC №3 ", "DESCRIPTION №3",
                Status.NEW, epic3.getId(), Duration.ofMinutes(23),
                LocalDateTime.of(2025, 3, 19, 21, 30));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        manager.printAllTasks();

    }
}
