package ru.practicum.task.service;

import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.service.manager.InMemoryTaskManager;
import ru.practicum.task.service.manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T getDefaultTaskManager();

    protected TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        manager = getDefaultTaskManager();
    }

    @Test
    public void testManagerShouldAddAndRetrieveTaskById() {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        manager.addTask(task1);

        Optional<Task> retrievedTask1 = manager.getTaskById(task1.getId());

        assertNotNull(retrievedTask1.get(), "Задача не должна возвращать null");
        assertEquals(task1, retrievedTask1.get(), "Извлеченная задача не соответствует добавленной");
    }

    @Test
    public void testManagerShouldAddAndRetrieveEpicById() {
        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        manager.addEpic(epic1);

        Optional<Epic> retrievedEpic1 = manager.getEpicById(epic1.getId());

        assertNotNull(retrievedEpic1.get(), "Эпик не должен возвращать null");
        assertEquals(epic1, retrievedEpic1.get(), "Извлеченный эпик не соответствует добавленному");
    }

    @Test
    public void testManagerShouldAddAndRetrieveSubtaskById() {
        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 3, 17, 17, 0));
        manager.addSubtask(subtask1);

        Optional<Subtask> retrievedSubtask1 = manager.getSubtaskById(subtask1.getId());

        assertNotNull(retrievedSubtask1.get(), "Подзадача не должна возвращать null");
        assertEquals(subtask1, retrievedSubtask1.get(), "Извлеченная подзадача не соответствует добавленной");
    }

    @Test
    public void testTaskDataShouldNotChange_whenAddedToTheManager() {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 3, 17, 10, 0));

        manager.addTask(task1);
        Optional<Task> retrievedTask = manager.getTaskById(task1.getId());

        assertEquals(task1.getName(), retrievedTask.get().getName(), "Название не должно изменяться");
        assertEquals(task1.getDescription(), retrievedTask.get().getDescription(), "Описание не должно изменяться");
        assertEquals(task1.getStatus(), retrievedTask.get().getStatus(), "Статус не должен изменяться");
        assertEquals(task1.getId(), retrievedTask.get().getId(), "ID не должен изменяться");
    }

    @Test
    public void testShouldPreserveTaskDataInHistoryManager() {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        manager.addTask(task1);

        manager.getTaskById(task1.getId());
        List<Task> history = manager.getHistory();

        assertNotNull(history, "История не должна возвращать null");
        assertEquals(1, history.size(), "История должна содержать одну задачу");
        assertEquals(task1, history.getFirst(), "Задача в истории должна соответствовать добавленной");
        assertEquals("TASK №1", history.getFirst().getName(), "Название в истории должно" +
                " соответствовать исходному");
        assertEquals("DESCRIPTION №1", history.getFirst().getDescription(), "Описание в истории" +
                " должно соответствовать исходному");
    }

    @Test
    void testManagerShouldClearAllTask() {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2025, 3, 19, 10, 0));
        manager.addTask(task1);
        manager.addTask(task2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        assertEquals(2, manager.getHistory().size(), "Размер не соответствует действительному");
        assertEquals(2, manager.getAllTasks().size(), "Размер не соответствует действительному");

        manager.clearAllTasks();

        assertEquals(0, manager.getHistory().size(), "Размер не соответствует действительному");
        assertEquals(0, manager.getAllTasks().size(), "Размер не соответствует действительному");
    }

}
