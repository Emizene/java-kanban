package ru.practicum.task.service;

import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.FileBackedTaskManager;
import ru.practicum.task.service.manager.ManagerSaveException;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    File temp = File.createTempFile("test", ".csv");
    FileBackedTaskManager manager = new FileBackedTaskManager(temp);

    FileBackedTaskManagerTest() throws IOException {
    }

    @Test
    void testShouldSaveAndLoadEmptyFile() throws ManagerSaveException {
        assertTrue(manager.getAllTasks().isEmpty(), "Список задач должен быть пустым.");
        assertTrue(manager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым.");
        assertTrue(manager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым.");
    }

    @Test
    void testShouldSaveAndUploadTasks() throws ManagerSaveException {
        Task task1 = new Task("TASK №1", "DESCRIPTION №1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.of(2025, 3, 17, 10, 0));
        Task task2 = new Task("TASK №2", "DESCRIPTION №2", Status.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(2025, 3, 19, 10, 0));
        manager.addTask(task1);
        manager.addTask(task2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(temp);
        assertArrayEquals(manager.getAllTasks().toArray(), loadedManager.getAllTasks().toArray());

    }

    @Test
    void test() {
        File file = new File("Not found path");
        assertThrows(ManagerSaveException.class, () -> new FileBackedTaskManager(file));
    }

}


