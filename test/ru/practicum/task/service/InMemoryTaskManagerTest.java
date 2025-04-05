package ru.practicum.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.BaseTest;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends BaseTest {

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    void testIntersectionTasksShouldNotBeAdded() {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2025, 3, 17, 10, 0));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        assertEquals(1, taskManager.getAllTasks().size(), "Размер не соответствует действительному");
    }

    @Test
    void testIntersectionCheckWorkCorrectly() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(30),
                LocalDateTime.of(2025, 3, 15, 19, 0));
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2025, 3, 15, 19, 0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        assertEquals(1, taskManager.getAllTasks().size(), "Размер не соответствует действительному");

        Epic epic1 = new Epic(3, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Epic epic2 = new Epic(4, "EPIC №2", "DESCRIPTION №2", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));

        Subtask subtask1 = new Subtask(5, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        Subtask subtask2 = new Subtask(6, "SUBTASK №2 EPIC №1", "DESCRIPTION №2",
                Status.NEW, Duration.ofMinutes(160), LocalDateTime.of(2025, 3,
                16, 16, 0), epic2.getId());
        epic1.addSubtask(subtask1);
        epic2.addSubtask(subtask2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(1, taskManager.getAllEpics().size(), taskManager.getAllSubtasks().size(),
                "Размер не соответствует действительному");

    }

    @Test
    void testDurationShouldBeCalculateCorrect() {
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2025, 3, 17, 17, 0), epic1.getId());
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);

        assertEquals(60, taskManager.getAllEpics().getFirst().getDuration().toMinutes());

        Subtask subtask2 = new Subtask(4, "SUBTASK №2 EPIC №1 ", "DESCRIPTION №2",
                Status.NEW, Duration.ofMinutes(25),
                LocalDateTime.of(2025, 3, 17, 20, 0), epic1.getId());
        taskManager.addSubtask(subtask2);

        assertEquals(85, taskManager.getAllEpics().getFirst().getDuration().toMinutes());
    }

    @Test
    void testShouldGetCorrectEndTime() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Epic epic1 = new Epic(2, "EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Subtask subtask1 = new Subtask(3, "SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2025, 3,
                17, 17, 0), epic1.getId());
        Subtask subtask2 = new Subtask(4, "SUBTASK №2 EPIC №1 ", "DESCRIPTION №2",
                Status.NEW, Duration.ofMinutes(25), LocalDateTime.of(2025, 3,
                17, 20, 0), epic1.getId());
        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);
        taskManager.addTask(task1);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(task1.getEndTime(), LocalDateTime.of(2025, 3, 17, 10, 10));
        assertEquals(epic1.getEndTime(), LocalDateTime.of(2025, 3, 17, 20, 25));
        assertEquals(subtask1.getEndTime(), LocalDateTime.of(2025, 3, 17, 18, 0));
        assertEquals(subtask2.getEndTime(), LocalDateTime.of(2025, 3, 17, 20, 25));
    }

}
