package ru.practicum.task.model;

import org.junit.jupiter.api.Test;
import ru.practicum.task.service.Managers;
import ru.practicum.task.service.manager.TaskManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void testEqualsTrue_whenTaskIdIsTheSame() {
        Task task1 = new Task("TEST TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task("TEST TASK №2", "DESCRIPTION №2", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "ID не равны");
        assertEquals(task1, task2, "Экземпляры класса не равны");
    }

    @Test
    void testShouldNotConflictBetweenManualSetIdAndGeneratedId() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        Task task1 = new Task("TEST TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task("TEST TASK №2", "DESCRIPTION №2", Status.NEW);
        task1.setId(999);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Optional<Task> manualSetId = taskManager.getTaskById(task1.getId());
        Optional<Task> generatedId = taskManager.getTaskById(task2.getId());

        assertNotNull(task1, "Задача с заданным ID не должна возвращать null");
        assertNotNull(task2, "Задача со сгенерированным ID не должна возвращать null");
        assertNotEquals(manualSetId, generatedId, "Задачи конфликтуют между собой");
    }

}