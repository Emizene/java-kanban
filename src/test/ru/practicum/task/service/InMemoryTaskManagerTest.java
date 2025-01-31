package ru.practicum.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void testManagerShouldAddAndRetrieveTaskById() {

        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW);
        manager.addTask(task1);

        Task retrievedTask1 = manager.getTaskById(task1.getId());

        assertNotNull(retrievedTask1, "Задача не должна возвращать null");
        assertEquals(task1, retrievedTask1, "Извлеченная задача не соответствует добавленной");
    }

    @Test
    public void testManagerShouldAddAndRetrieveEpicById() {

        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW);
        manager.addEpic(epic1);

        Epic retrievedEpic1 = manager.getEpicById(epic1.getId());

        assertNotNull(retrievedEpic1, "Эпик не должен возвращать null");
        assertEquals(epic1, retrievedEpic1, "Извлеченный эпик не соответствует добавленному");
    }

    @Test
    public void testManagerShouldAddAndRetrieveSubtaskById() {

        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW);
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);

        Subtask retrievedSubtask1 = manager.getSubtaskById(subtask1.getId());

        assertNotNull(retrievedSubtask1, "Подзадача не должна возвращать null");
        assertEquals(subtask1, retrievedSubtask1, "Извлеченная подзадача не соответствует добавленной");
    }

    @Test
    public void testTaskDataShouldNotChange_whenAddedToTheManager() {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW);

        manager.addTask(task1);
        Task retrievedTask = manager.getTaskById(task1.getId());

        assertEquals(task1.getName(), retrievedTask.getName(), "Название не должно изменяться");
        assertEquals(task1.getDescription(), retrievedTask.getDescription(), "Описание не должно изменяться");
        assertEquals(task1.getStatus(), retrievedTask.getStatus(), "Статус не должен изменяться");
        assertEquals(task1.getId(), retrievedTask.getId(), "ID не должен изменяться");
    }

    @Test
    public void testShouldPreserveTaskDataInHistoryManager() {

        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW);
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

}
