package service;

import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Task;
import ru.practicum.task.service.HistoryManager;
import ru.practicum.task.service.Managers;
import ru.practicum.task.service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void testShouldReturnNotNullHistoryManager() {

        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        assertNotNull(historyManager, "Экземпляр HistoryManager не должен возвращать null");
    }

    @Test
    public void testShouldReturnNotNullTaskManager() {

        TaskManager taskManager = Managers.getDefaultTaskManager();

        assertNotNull(taskManager, "Экземпляр TaskManager не должен возвращать null");
    }

    @Test
    public void testShouldReturnNewTaskManagerInstanceEachTime() {

        TaskManager first = Managers.getDefaultTaskManager();
        TaskManager second = Managers.getDefaultTaskManager();

        assertNotSame(first, second, "Каждый вызов метода должен возвращать" +
                " новый экземпляр TaskManager");
    }

    @Test
    public void testShouldReturnNewHistoryManagerInstanceEachTime() {

        HistoryManager first = Managers.getDefaultHistoryManager();
        HistoryManager second = Managers.getDefaultHistoryManager();

        assertNotSame(first, second, "Каждый вызов метода должен" +
                " возвращать новый экземпляр HistoryManager");
    }


    @Test
    public void testHistoryManagerInstanceShouldBeFunctional() {

        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        Task task1 = new Task("TEST TASK №1", "DESCRIPTION №1", Status.NEW);
        historyManager.addInHistory(task1);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "List history не должна возвращать null");
        assertEquals(1, history.size(), "HistoryManager не корректно хранит задачи");
        assertEquals(task1, history.getFirst(), "HistoryManager должен корректно возвращать необходимую" +
                " задачу из истории");
    }

    @Test
    public void testTaskManagerInstanceShouldBeFunctional() {
        TaskManager taskManager = Managers.getDefaultTaskManager();

        Task task1 = new Task("TEST TASK №1", "DESCRIPTION №1", Status.NEW);

        taskManager.addTask(task1);

        assertEquals(1, taskManager.getAllTasks().size(), "TaskManager не корректно хранит задачи");
        assertEquals(task1, taskManager.getAllTasks().getFirst(), "TaskManager должен корректно возвращать" +
                " необходимую задачу");
    }

}