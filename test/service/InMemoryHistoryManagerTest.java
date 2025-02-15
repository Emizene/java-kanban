package service;

import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Test
    void testHistoryManagerShouldAddTask() {
        Task task1 = new Task(1, "TASK №1", "DESCRIPTION №1", Status.NEW);
        Task task2 = new Task(2, "TASK №2", "DESCRIPTION №2", Status.NEW);
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);

        assertEquals(2, historyManager.getTasks().size(), "Размер не соответствует действительному");
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
        historyManager.addInHistory(task1);
        historyManager.addInHistory(task2);

        assertEquals(2, historyManager.getTasks().size(), "Размер не соответствует действительному");

        historyManager.remove(1);

        assertEquals(1, historyManager.getTasks().size(), "Размер не соответствует действительному");
    }

}
