package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task.model.Epic;
import ru.practicum.task.model.Status;
import ru.practicum.task.model.Subtask;
import ru.practicum.task.service.manager.InMemoryTaskManager;
import ru.practicum.task.service.manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    protected TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void testEqualsTrue_whenEpicIdIsTheSame() {
        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        Epic epic2 = new Epic("EPIC №2", "DESCRIPTION №2", Status.NEW,
                LocalDateTime.of(2025, 3, 19, 19, 19));
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "ID не равны");
        assertEquals(epic1, epic2, "Экземпляры класса не равны");
    }

    @Test
    public void testEpicStatusShouldBeUpdate() {
        Epic epic1 = new Epic("EPIC №1", "DESCRIPTION №1", Status.NEW,
                LocalDateTime.of(2025, 3, 17, 16, 30));
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("SUBTASK №1 EPIC №1", "DESCRIPTION №1",
                Status.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(5),
                LocalDateTime.of(2025, 3, 17, 17, 0));
        manager.addSubtask(subtask1);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus());

        Subtask subtask2 = new Subtask("SUBTASK №2 EPIC №2 ", "DESCRIPTION №2",
                Status.DONE, epic1.getId(), Duration.ofMinutes(12),
                LocalDateTime.of(2025, 3, 19, 20, 0));
        manager.addSubtask(subtask2);
        manager.updateSubtaskStatus(subtask1, subtask2);

        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    public void testEqualsTrue_whenEpicCannotBecomeItIsOwnSubtask() {
        //Epic epic1 = new Epic("TEST EPIC №1", "DESCRIPTION №1", Status.NEW);
        //java: incompatible types: ru.practicum.task.model.Epic cannot be converted to ru.practicum.task.model.Subtask
        //В Epic нельзя добавить самого себя в качестве Subtask, так как метод addSubtask принимает только тип Subtask
        //epic1.addSubtask(epic1);
    }

}