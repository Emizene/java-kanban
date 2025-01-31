package ru.practicum.task.service;

import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void testHistoryManagerContainsOnlyTenTasks() {

        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("HISTORY TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task("HISTORY TASK №2", "DESCRIPTION №2", Status.NEW);
        Task task3 = new Task("HISTORY TASK №3", "DESCRIPTION №3", Status.NEW);
        Task task4 = new Task("HISTORY TASK №4", "DESCRIPTION №4", Status.NEW);
        Task task5 = new Task("HISTORY TASK №5", "DESCRIPTION №5", Status.NEW);
        Task task6 = new Task("HISTORY TASK №6", "DESCRIPTION №6", Status.NEW);
        Task task7 = new Task("HISTORY TASK №7", "DESCRIPTION №7", Status.NEW);
        Task task8 = new Task("HISTORY TASK №8", "DESCRIPTION №8", Status.NEW);
        Task task9 = new Task("HISTORY TASK №9", "DESCRIPTION №9", Status.NEW);
        Task task10 = new Task("HISTORY TASK №10", "DESCRIPTION №10", Status.NEW);
        Task task11 = new Task("HISTORY TASK №11", "DESCRIPTION №11", Status.NEW);

        historyManager.addTaskInHistory(task1);

        assertTrue(historyManager.getHistory().contains(task1));

        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
        historyManager.addTaskInHistory(task4);
        historyManager.addTaskInHistory(task5);
        historyManager.addTaskInHistory(task6);
        historyManager.addTaskInHistory(task7);
        historyManager.addTaskInHistory(task8);
        historyManager.addTaskInHistory(task9);
        historyManager.addTaskInHistory(task10);
        historyManager.addTaskInHistory(task11);

        assertFalse(historyManager.getHistory().contains(task1));

    }

}