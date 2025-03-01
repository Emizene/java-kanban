package service;

import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.manager.FileBackedTaskManager;
import ru.practicum.task.service.manager.ManagerSaveException;

import java.io.File;
import java.io.IOException;

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
        Task task1 = new Task(1,"TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task(2,"TASK №2", "DESCRIPTION №2", Status.NEW);
        manager.addTask(task1);
        manager.addTask(task2);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(temp);
        assertArrayEquals(manager.getAllTasks().toArray(), loadedManager.getAllTasks().toArray());

    }
}


