package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.history.HistoryManager;
import ru.practicum.task.service.manager.InMemoryTaskManager;
import ru.practicum.task.service.manager.ManagerSaveException;
import ru.practicum.task.service.manager.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class HistoryManagerTest <T extends HistoryManager> {
    protected abstract T getDefaultHistoryManager();

    protected HistoryManager historyManager;
    protected TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        historyManager = getDefaultHistoryManager();
    }

    @Test
    void testHistoryManagerShouldAddTask() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);

        assertEquals(2, historyManager.getHistory().size(), "Размер не соответствует действительному");
    }

    @Test
    public void testHistoryManagerShouldNotBeNull() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);

        assertNotNull(task1, "Задача не должна возвращать null");
        assertNotNull(task2, "Задача не должна возвращать null");
    }

    @Test
    void testHistoryManagerShouldDeleteTask() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW);
        Task task3= new Task(3, "TASK №3", "DESCRIPTION №3", Status.NEW);
        Task task4= new Task(4, "TASK №4", "DESCRIPTION №4", Status.NEW);
        Task task5= new Task(5, "TASK №5", "DESCRIPTION №5", Status.NEW);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);
        historyManager.addInHistory(task3);
        historyManager.addInHistory(task4);
        historyManager.addInHistory(task5);

        assertEquals(5, historyManager.getHistory().size(), "Размер не соответствует действительному");

        historyManager.remove(1);
        historyManager.remove(3);
        historyManager.remove(5);

        assertEquals(2, historyManager.getHistory().size(), "Размер не соответствует действительному");
    }

    @Test
    void testHistoryManagerShouldClearAllHistory() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);

        historyManager.clearAllHistory();

        assertEquals(0, historyManager.getHistory().size(), "Размер не соответствует действительному");
    }

    @Test
    void testHistoryManagerShouldRemainOnlyTheLastView() throws ManagerSaveException {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task1);

        assertEquals(1, historyManager.getHistory().size(), "Размер не соответствует действительному");
    }
}