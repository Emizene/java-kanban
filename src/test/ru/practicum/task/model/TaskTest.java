package ru.practicum.task.model;

import org.junit.jupiter.api.Test;
import ru.practicum.task.service.Managers;
import ru.practicum.task.service.TaskManager;

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
    void testEqualsTrue_whenIdSetManual() {
        TaskManager manager = Managers.getDefaultTaskManager();
        Task task1 = new Task("TEST TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task("TEST TASK №2", "DESCRIPTION №2", Status.NEW);
        task1.setId(999);
        manager.addTask(task1);
        manager.addTask(task2);

        assertNotNull(task1, "Задача с заданным ID не должна возвращать null");
        assertNotNull(task2, "Задача со сгенерированным ID не должна возвращать null");
        assertEquals(task1, manager.getTaskById(999), "ID установлен не корректно");
        assertEquals(task2, manager.getTaskById(1), "ID установлен не корректно");
    }

}